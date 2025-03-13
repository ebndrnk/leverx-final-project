package org.ebndrnk.leverxfinalproject.repository.specification;

import org.ebndrnk.leverxfinalproject.model.entity.game.GameObject;
import org.springframework.data.jpa.domain.Specification;

/**
 * This class contains specifications for querying the GameObject entity.
 * It provides common filtering functionality based on the attributes of the GameObject.
 */
public class GameObjectSpecification {

    /**
     * Creates a Specification to filter GameObjects by title.
     * It performs a case-insensitive "LIKE" query to match the given title pattern.
     *
     * @param title The title to filter by. Can be null to disable this filter.
     * @return The Specification for filtering GameObjects by title.
     */
    public static Specification<GameObject> hasTitleLike(String title) {
        return (root, query, cb) ->
                title == null ? null : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    /**
     * Creates a Specification to filter GameObjects by text.
     * It performs a case-insensitive "LIKE" query to match the given text pattern.
     *
     * @param text The text to filter by. Can be null to disable this filter.
     * @return The Specification for filtering GameObjects by text.
     */
    public static Specification<GameObject> hasTextLike(String text) {
        return (root, query, cb) ->
                text == null ? null : cb.like(cb.lower(root.get("text")), "%" + text.toLowerCase() + "%");
    }

}
