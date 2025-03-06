package org.ebndrnk.leverxfinalproject.service.comment;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.entity.comment.CommentAuthor;
import org.ebndrnk.leverxfinalproject.repository.comment.CommentAuthorRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.CommentAuthorNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentAuthorService {
    private final CommentAuthorRepository commentAuthorRepository;

    public CommentAuthor getCommentAuthor(String identifier) {
        return commentAuthorRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new CommentAuthorNotFoundException("Author not found: " + identifier));
    }

    public CommentAuthor createNewAuthor(String identifier) {
        CommentAuthor author = new CommentAuthor();
        author.setIdentifier(identifier);
        return commentAuthorRepository.save(author);
    }
}
