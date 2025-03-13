package org.ebndrnk.leverxfinalproject.service.comment.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentDto;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentRequest;
import org.ebndrnk.leverxfinalproject.model.entity.comment.seller.SellerFromComment;
import org.ebndrnk.leverxfinalproject.repository.comment.seller.SellerFromCommentRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.SellerFromCommentNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling operations related to sellers from comments.
 */
@Service
@Slf4j
@Primary
@RequiredArgsConstructor
public class SellerFromCommentServiceImpl implements SellerFromCommentService {

    private final SellerFromCommentRepository sellerFromCommentRepository;
    private final ModelMapper modelMapper;

    /**
     * Saves information about a seller from a comment.
     *
     * @param sellerFromCommentDto DTO containing seller data to be saved.
     * @return Saved seller information in the form of a DTO.
     */
    @Override
    public SellerFromCommentDto saveSellerFromCommentInfo(SellerFromCommentDto sellerFromCommentDto) {
        log.info("Saving seller from comment information: {}", sellerFromCommentDto);
        SellerFromComment savedSeller = sellerFromCommentRepository.save(modelMapper.map(sellerFromCommentDto, SellerFromComment.class));
        log.info("Seller from comment saved with ID: {}", savedSeller.getId());
        return modelMapper.map(savedSeller, SellerFromCommentDto.class);
    }

    /**
     * Retrieves seller information based on the provided email.
     *
     * @param email Email of the seller to retrieve.
     * @return Seller information in the form of a DTO.
     * @throws SellerFromCommentNotFoundException if no seller is found with the provided email.
     */
    @Override
    public SellerFromCommentDto getByEmail(String email) {
        log.info("Retrieving seller by email: {}", email);
        SellerFromComment seller = sellerFromCommentRepository.findByEmail(email)
                .orElseThrow(() -> new SellerFromCommentNotFoundException("Seller with this email not found"));
        log.info("Seller found with email: {}", email);
        return modelMapper.map(seller, SellerFromCommentDto.class);
    }

    /**
     * Retrieves seller information based on the provided username.
     *
     * @param username Username of the seller to retrieve.
     * @return Seller information in the form of a DTO.
     * @throws SellerFromCommentNotFoundException if no seller is found with the provided username.
     */
    @Override
    public SellerFromCommentDto getByUsername(String username) {
        log.info("Retrieving seller by username: {}", username);
        SellerFromComment seller = sellerFromCommentRepository.findByEmail(username)
                .orElseThrow(() -> new SellerFromCommentNotFoundException("Seller with this username not found"));
        log.info("Seller found with username: {}", username);
        return modelMapper.map(seller, SellerFromCommentDto.class);
    }

    /**
     * Checks if a seller exists by email or username.
     *
     * @param seller Request object containing the email and/or username to check.
     * @return True if a seller exists with the provided email or username, false otherwise.
     */
    @Override
    public boolean isExists(SellerFromCommentRequest seller) {
        boolean isExistsByEmail = false;
        if(seller.getEmail() != null){
            isExistsByEmail = sellerFromCommentRepository.existsByEmail(seller.getEmail());
        }

        boolean isExistsByUsername = false;
        if(seller.getUsername() != null){
            isExistsByUsername = sellerFromCommentRepository.existsByUsername(seller.getUsername());
        }

        boolean exists = isExistsByEmail || isExistsByUsername;
        log.info("Checking if seller exists with email {} or username {}: {}", seller.getEmail(), seller.getUsername(), exists);
        return exists;
    }
}
