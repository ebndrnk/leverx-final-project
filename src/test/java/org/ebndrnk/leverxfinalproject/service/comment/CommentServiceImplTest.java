package org.ebndrnk.leverxfinalproject.service.comment;

import jakarta.servlet.http.HttpServletRequest;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;
import org.ebndrnk.leverxfinalproject.model.entity.anonymous.AnonymousUser;
import org.ebndrnk.leverxfinalproject.model.entity.comment.Comment;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.repository.comment.CommentRepository;
import org.ebndrnk.leverxfinalproject.repository.pofile.ProfileRepository;
import org.ebndrnk.leverxfinalproject.service.anonymous.AnonymousUserService;
import org.ebndrnk.leverxfinalproject.service.auth.user.UserService;
import org.ebndrnk.leverxfinalproject.service.comment.seller.SellerFromCommentService;
import org.ebndrnk.leverxfinalproject.service.profile.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private AnonymousUserService anonymousUserService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddComment_shouldCreateComment() {
        // Arrange
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setMessage("Test message");
        Long sellerId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        Profile seller = mock(Profile.class);
        AnonymousUser author = mock(AnonymousUser.class);
        Comment savedComment = mock(Comment.class);
        CommentResponse expectedResponse = new CommentResponse();

        when(profileRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        when(anonymousUserService.getOrCreateAnonymousUser(request)).thenReturn(author);
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);
        when(modelMapper.map(savedComment, CommentResponse.class)).thenReturn(expectedResponse);

        // Act
        CommentResponse result = commentService.addComment(commentRequest, sellerId, request);

        // Assert
        assertNotNull(result);
        verify(commentRepository).save(any(Comment.class));
    }




}
