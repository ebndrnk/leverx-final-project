package org.ebndrnk.leverxfinalproject.service.comment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentDto;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;
import org.ebndrnk.leverxfinalproject.model.entity.anonymous.AnonymousUser;
import org.ebndrnk.leverxfinalproject.model.entity.comment.Comment;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.repository.comment.CommentRepository;
import org.ebndrnk.leverxfinalproject.repository.pofile.ProfileRepository;
import org.ebndrnk.leverxfinalproject.service.anonymous.AnonymousUserService;
import org.ebndrnk.leverxfinalproject.service.account.user.UserService;
import org.ebndrnk.leverxfinalproject.service.comment.seller.SellerFromCommentService;
import org.ebndrnk.leverxfinalproject.service.profile.ProfileService;
import org.ebndrnk.leverxfinalproject.util.exception.dto.CommentNotFoundException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.NoAuthorityForActionException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.ProfileNotFoundException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@Primary
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ProfileRepository profileRepository;
    private final SellerFromCommentService sellerFromCommentService;
    private final ProfileService profileService;
    private final AnonymousUserService anonymousUserService;


    /**
     * Adds a new comment for a seller by an anonymous user.
     *
     * @param commentRequest the comment request containing the message
     * @param sellerId       the ID of the seller
     * @param request        the HTTP request object
     * @return the added comment response
     */
    @Override
    public CommentResponse addComment(CommentRequest commentRequest, Long sellerId, HttpServletRequest request) {
        Profile seller = profileRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Seller not found with id: " + sellerId));

        AnonymousUser author = anonymousUserService.getOrCreateAnonymousUser(request);

        log.info("Adding comment for seller with id: {}", sellerId);
        return createComment(commentRequest.getMessage(), seller, author);
    }

    /**
     * Adds a new comment with a seller by an anonymous user.
     *
     * @param sellerFromCommentRequest the seller's comment request
     * @param request                  the HTTP request object
     * @return the added comment response
     */
    @Override
    public CommentResponse addComment(SellerFromCommentRequest sellerFromCommentRequest, HttpServletRequest request) {
        Profile seller = modelMapper.map(getOrCreateSellerFromComment(sellerFromCommentRequest), Profile.class);

        AnonymousUser author = anonymousUserService.getOrCreateAnonymousUser(request);

        log.info("Adding comment from seller: {}", sellerFromCommentRequest.getEmail());
        return createComment(sellerFromCommentRequest.getMessage(), seller, author);
    }

    /**
     * Retrieves or creates a seller's profile from the comment request.
     *
     * @param request the seller comment request
     * @return the created or retrieved profile
     */
    private ProfileDto getOrCreateSellerFromComment(SellerFromCommentRequest request) {
        Optional<ProfileDto> profileOptional = findExistingProfile(request);
        if (profileOptional.isPresent()) {
            return profileOptional.get();
        }

        ProfileDto profileDto = profileService.saveProfileInfo(modelMapper.map(request, ProfileDto.class));
        SellerFromCommentDto sellerDto = modelMapper.map(request, SellerFromCommentDto.class);
        sellerDto.setProfile(profileDto);
        sellerFromCommentService.saveSellerFromCommentInfo(sellerDto);
        return profileDto;
    }

    /**
     * Finds an existing profile based on the email or username from the request.
     *
     * @param request the seller comment request
     * @return an optional containing the profile, or empty if not found
     */
    private Optional<ProfileDto> findExistingProfile(SellerFromCommentRequest request) {
        if (sellerFromCommentService.isExists(request)) {
            try {
                return Optional.of(profileService.getProfileByEmail(request.getEmail()));
            } catch (ProfileNotFoundException e) {
                try {
                    return Optional.of(profileService.getProfileByUsername(request.getUsername()));
                } catch (ProfileNotFoundException ex) {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Creates a new comment and saves it to the database.
     *
     * @param message the message of the comment
     * @param seller  the seller the comment is for
     * @param author  the anonymous user authoring the comment
     * @return the created comment response
     */
    private CommentResponse createComment(String message, Profile seller, AnonymousUser author){
        Comment comment = new Comment();
        comment.setMessage(message);
        comment.setSeller(seller);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);
        CommentResponse commentResponse = modelMapper.map(savedComment, CommentResponse.class);
        commentResponse.setSellerId(seller.getId());

        log.info("Comment created for seller with id: {}", seller.getId());
        return commentResponse;
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param commentId the ID of the comment to delete
     * @param request   the HTTP request object
     */
    @Override
    public void deleteById(Long commentId, HttpServletRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with this id not found"));

        try {
            if (userService.getCurrentUser() != null &&
                    !Objects.equals(comment.getSeller().getId(), userService.getCurrentUser().getId())) {
                throw new NoAuthorityForActionException("You have no authority for this action");
            } else {
                commentRepository.deleteById(commentId);
                log.info("Comment with id: {} was deleted by seller", commentId);
            }
        } catch (UserNotFoundException e) {
            AnonymousUser author = anonymousUserService.getOrCreateAnonymousUser(request);
            if (!Objects.equals(comment.getAuthor().getAnonymousId(), author.getAnonymousId())) {
                throw new NoAuthorityForActionException("You have no authority for this action");
            } else {
                commentRepository.deleteById(commentId);
                log.info("Comment with id: {} was deleted by anonymous user which published this comment", commentId);
            }
        }
    }

    /**
     * method for admin only
     * @param commentId
     */
    @Override
    public void deleteById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with this id not found"));

        commentRepository.deleteById(commentId);
    }

    /**
     * Retrieves all comments for a specific seller.
     *
     * @param userId the ID of the seller
     * @return a list of comment responses
     */
    @Override
    public List<CommentResponse> getSellersComments(Long userId) {
        profileRepository.findById(userId).orElseThrow(() -> new ProfileNotFoundException("Profile with this id not found"));

        log.info("Retrieving all comments for seller with id: {}", userId);
        return commentRepository.findAllBySeller_Id(userId).stream()
                .map(c -> modelMapper.map(c, CommentResponse.class))
                .toList();
    }

    /**
     * Retrieves a specific comment by its ID.
     *
     * @param commentId the ID of the comment
     * @return the comment response
     */
    @Override
    public CommentResponse getCommentById(Long commentId) {
        log.info("Retrieving comment with id: {}", commentId);
        return modelMapper.map(commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with this id not found")), CommentResponse.class);
    }

    /**
     * Edits an existing comment.
     *
     * @param commentId      the ID of the comment to edit
     * @param commentRequest the updated comment request
     * @param request        the HTTP request object
     * @return the updated comment response
     */
    @Override
    public CommentResponse editComment(Long commentId, CommentRequest commentRequest, HttpServletRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with this id not found"));

        AnonymousUser author = anonymousUserService.getOrCreateAnonymousUser(request);
        if (!Objects.equals(comment.getAuthor().getAnonymousId(), author.getAnonymousId())) {
            throw new NoAuthorityForActionException("You have no authority for this action");
        } else {
            comment.setMessage(commentRequest.getMessage());
            CommentResponse commentResponse = modelMapper.map(commentRepository.save(comment), CommentResponse.class);
            log.info("Comment with id: {} was edited by anonymous user which published this comment", commentId);
            return commentResponse;
        }
    }

    /**
     * Retrieves all unconfirmed comments.
     *
     * @return a list of unconfirmed comment responses
     */
    @Override
    public List<CommentResponse> getAllUnconfirmed() {
        log.info("Retrieving all unconfirmed comments");
        return commentRepository.findAllUnconfirmed().stream()
                .map(c -> modelMapper.map(c, CommentResponse.class))
                .toList();
    }

    /**
     * Retrieves all confirmed comments.
     *
     * @return a list of confirmed comment responses
     */
    @Override
    public List<CommentResponse> getAllConfirmed() {
        log.info("Retrieving all confirmed comments");
        return commentRepository.findAllConfirmed().stream()
                .map(c -> modelMapper.map(c, CommentResponse.class))
                .toList();
    }

    /**
     * Confirms a comment by its ID.
     *
     * @param commentId the ID of the comment to confirm
     * @return the confirmed comment response
     */
    @Override
    public CommentResponse confirm(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("comment with this id not found"));
        comment.setApproved(true);

        log.info("Comment with id: {} has been confirmed", commentId);
        return modelMapper.map(commentRepository.save(comment), CommentResponse.class);
    }

    /**
     * Declines a comment by its ID.
     *
     * @param commentId the ID of the comment to decline
     * @return the declined comment response
     */
    @Override
    public CommentResponse decline(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("comment with this id not found"));
        comment.setApproved(false);

        log.info("Comment with id: {} has been declined", commentId);
        return modelMapper.map(commentRepository.save(comment), CommentResponse.class);
    }
}
