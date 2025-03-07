package org.ebndrnk.leverxfinalproject.service.comment;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserResponse;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.comment.Comment;
import org.ebndrnk.leverxfinalproject.model.entity.comment.CommentAuthor;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.repository.comment.CommentRepository;
import org.ebndrnk.leverxfinalproject.service.auth.UserService;
import org.ebndrnk.leverxfinalproject.util.exception.dto.CommentAuthorNotFoundException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.CommentNotFoundException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.NoAuthorityForActionException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.ebndrnk.leverxfinalproject.util.request.RequestInfoUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final RequestInfoUtil requestInfoUtil;
    private final CommentAuthorService commentAuthorService;
    private final UserRepository userRepository;
    private final UserService userService;



    @Override
    public CommentResponse addComment(CommentRequest commentRequest, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Seller not found with id: " + sellerId));

        CommentAuthor author = getOrCreateAuthor();

        Comment comment = new Comment();
        comment.setMessage(commentRequest.getMessage());
        comment.setSeller(seller);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);
        CommentResponse commentResponse = modelMapper.map(savedComment, CommentResponse.class);
        commentResponse.setSeller(modelMapper.map(comment.getSeller(), UserResponse.class));
        return commentResponse;
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



    @Override
    public void deleteById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment with this id not found"));
        try {
            if(userService.getCurrentUser() != null && !Objects.equals(comment.getSeller().getId(), userService.getCurrentUser().getId())){
                throw new NoAuthorityForActionException("You have no authority for this action");

            }else {
                commentRepository.deleteById(commentId);
                log.info("Comment with id: {} was deleted by seller", commentId);
            }
        } catch (UserNotFoundException e){
            if(!Objects.equals(comment.getAuthor().getIdentifier(), requestInfoUtil.getInfoFromRequest())){
                throw new NoAuthorityForActionException("You have no authority for this action");
            } else{
                commentRepository.deleteById(commentId);
                log.info("Comment with id: {} was deleted by anonymous user which published this comment", commentId);
            }
        }

    }

    @Override
    public List<CommentResponse> getSellersComments(Long userId) {
        return commentRepository.findAllBySeller_Id(userId).stream()
                .map(c -> modelMapper.map(c, CommentResponse.class))
                .toList();
    }


    @Override
    public CommentResponse getCommentById(Long commentId) {
        return modelMapper.map(commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment with this id not found")), CommentResponse.class);
    }

    @Override
    public CommentResponse editComment(Long commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment with this id not found"));
        if(!Objects.equals(comment.getAuthor().getIdentifier(), requestInfoUtil.getInfoFromRequest())){
            throw new NoAuthorityForActionException("You have no authority for this action");
        }
        else {
            comment.setMessage(commentRequest.getMessage());
            CommentResponse commentResponse = modelMapper.map(commentRepository.save(comment), CommentResponse.class);
            log.info("Comment with id: {} was edited by anonymous user which published this comment", commentId);
            return commentResponse;
        }
    }


    @Override
    public List<CommentResponse> getAllUnconfirmed() {
        return commentRepository.findAllUnconfirmed().stream()
                .map(c -> modelMapper.map(c, CommentResponse.class))
                .toList();
    }

    @Override
    public List<CommentResponse> getAllConfirmed() {
        return commentRepository.findAllConfirmed().stream()
                .map(c -> modelMapper.map(c, CommentResponse.class))
                .toList();
    }

    @Override
    public CommentResponse confirm(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("comment with this id not found"));
        comment.setApproved(true);
        return modelMapper.map(commentRepository.save(comment), CommentResponse.class);
    }


    @Override
    public CommentResponse decline(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("comment with this id not found"));
        comment.setApproved(false);
        return modelMapper.map(commentRepository.save(comment), CommentResponse.class);
    }
}
