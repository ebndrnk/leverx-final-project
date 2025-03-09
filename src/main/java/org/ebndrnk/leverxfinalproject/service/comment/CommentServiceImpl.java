package org.ebndrnk.leverxfinalproject.service.comment;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentDto;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;
import org.ebndrnk.leverxfinalproject.model.entity.comment.Comment;
import org.ebndrnk.leverxfinalproject.model.entity.comment.CommentAuthor;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.repository.comment.CommentRepository;
import org.ebndrnk.leverxfinalproject.repository.pofile.ProfileRepository;
import org.ebndrnk.leverxfinalproject.service.auth.user.UserService;
import org.ebndrnk.leverxfinalproject.service.comment.seller.SellerFromCommentService;
import org.ebndrnk.leverxfinalproject.service.profile.ProfileService;
import org.ebndrnk.leverxfinalproject.util.exception.dto.*;
import org.ebndrnk.leverxfinalproject.util.request.RequestInfoUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final RequestInfoUtil requestInfoUtil;
    private final CommentAuthorService commentAuthorService;
    private final UserService userService;
    private final ProfileRepository profileRepository;
    private final SellerFromCommentService sellerFromCommentService;
    private final ProfileService profileService;

    @Override
    public CommentResponse addComment(CommentRequest commentRequest, Long sellerId) {
        Profile seller = profileRepository.findById(sellerId).orElseThrow(() -> new UserNotFoundException("Seller not found with id: " + sellerId));

        CommentAuthor author = getOrCreateAuthor();

        return createComment(commentRequest.getMessage(), seller, author);
    }


    @Override
    public CommentResponse addComment(SellerFromCommentRequest sellerFromCommentRequest) {
        Profile seller = modelMapper.map(getOrCreateSellerFromComment(sellerFromCommentRequest), Profile.class);

        CommentAuthor author = getOrCreateAuthor();


        return createComment(sellerFromCommentRequest.getMessage(), seller, author);
    }


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



    private CommentResponse createComment(String message, Profile seller, CommentAuthor author){
        Comment comment = new Comment();
        comment.setMessage(message);
        comment.setSeller(seller);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);
        CommentResponse commentResponse = modelMapper.map(savedComment, CommentResponse.class);
        commentResponse.setSellerId(seller.getId());

        return modelMapper.map(savedComment, CommentResponse.class);
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
        profileRepository.findById(userId).orElseThrow(() -> new ProfileNotFoundException("Profile with this id not found"));

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
