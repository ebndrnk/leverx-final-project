package org.ebndrnk.leverxfinalproject.service.comment;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserDto;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentDto;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.comment.Comment;
import org.ebndrnk.leverxfinalproject.model.entity.comment.CommentAuthor;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.repository.comment.CommentRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.CommentAuthorNotFoundException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.ebndrnk.leverxfinalproject.util.request.RequestInfoUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final RequestInfoUtil requestInfoUtil;
    private final CommentAuthorService commentAuthorService;
    private final UserRepository userRepository;

    @Override
    public CommentDto addComment(CommentRequest commentRequest, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Seller not found with id: " + sellerId));

        CommentAuthor author = getOrCreateAuthor();

        Comment comment = new Comment();
        comment.setMessage(commentRequest.getMessage());
        comment.setSeller(seller);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);

        return convertToDto(savedComment);
    }

    private CommentAuthor getOrCreateAuthor() {
        String identifier = requestInfoUtil.getInfoFromRequest();
        try {
            return commentAuthorService.getCommentAuthor(identifier);
        } catch (CommentAuthorNotFoundException e) {
            log.info("Creating new comment author with identifier: {}", identifier);
            return commentAuthorService.createNewAuthor(identifier);
        }
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
        dto.setSeller(modelMapper.map(comment.getSeller(), UserDto.class));
        return dto;
    }
}
