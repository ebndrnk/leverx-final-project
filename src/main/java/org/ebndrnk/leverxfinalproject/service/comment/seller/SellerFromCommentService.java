package org.ebndrnk.leverxfinalproject.service.comment.seller;

import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentDto;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentRequest;

public interface SellerFromCommentService {
    SellerFromCommentDto saveSellerFromCommentInfo(SellerFromCommentDto sellerFromCommentDto);

    SellerFromCommentDto getByEmail(String email);

    SellerFromCommentDto getByUsername(String username);

    boolean isExists(SellerFromCommentRequest seller);
}
