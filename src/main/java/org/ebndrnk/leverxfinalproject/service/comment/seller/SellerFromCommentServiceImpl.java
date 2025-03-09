package org.ebndrnk.leverxfinalproject.service.comment.seller;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentDto;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentRequest;
import org.ebndrnk.leverxfinalproject.model.entity.comment.seller.SellerFromComment;
import org.ebndrnk.leverxfinalproject.repository.comment.seller.SellerFromCommentRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.SellerFromCommentNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerFromCommentServiceImpl implements SellerFromCommentService {
    private final SellerFromCommentRepository sellerFromCommentRepository;
    private final ModelMapper modelMapper;

    @Override
    public SellerFromCommentDto saveSellerFromCommentInfo(SellerFromCommentDto sellerFromCommentDto){
        return modelMapper.map(sellerFromCommentRepository.save(modelMapper.map(sellerFromCommentDto, SellerFromComment.class)), SellerFromCommentDto.class);
    }

    @Override
    public SellerFromCommentDto getByEmail(String email){
        SellerFromComment seller = sellerFromCommentRepository.findByEmail(email)
                .orElseThrow(() -> new SellerFromCommentNotFoundException("Seller with this email not found"));
        return modelMapper.map(seller, SellerFromCommentDto.class);
    }

    @Override
    public SellerFromCommentDto getByUsername(String username){
        SellerFromComment seller = sellerFromCommentRepository.findByEmail(username)
                .orElseThrow(() -> new SellerFromCommentNotFoundException("Seller with this username not found"));
        return modelMapper.map(seller, SellerFromCommentDto.class);
    }

    @Override
    public boolean isExists(SellerFromCommentRequest seller){
        return sellerFromCommentRepository.existsByEmail(seller.getEmail())
                || sellerFromCommentRepository.existsByUsername(seller.getUsername());
    }
}
