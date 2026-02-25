package com.challenge.ecommercebackend.modules.admin.domain.usecase;
import com.challenge.ecommercebackend.modules.admin.web.dto.TopSellingProduct;
import java.util.List;
public interface GetTopSellingProductsUseCase {
    List<TopSellingProduct> execute(Integer limit);
}
