package org.ebndrnk.leverxfinalproject.repository.specification;

import org.ebndrnk.leverxfinalproject.model.entity.game.GameObject;
import org.springframework.data.jpa.domain.Specification;

public class GameObjectSpecification {

    public static Specification<GameObject> hasTitleLike(String title) {
        return (root, query, cb) ->
                title == null ? null : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<GameObject> hasTextLike(String text) {
        return (root, query, cb) ->
                text == null ? null : cb.like(cb.lower(root.get("text")), "%" + text.toLowerCase() + "%");
    }

}
