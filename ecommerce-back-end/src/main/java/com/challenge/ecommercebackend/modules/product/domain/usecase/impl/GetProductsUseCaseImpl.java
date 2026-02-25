package com.challenge.ecommercebackend.modules.product.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.product.domain.usecase.GetProductsUseCase;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.persisten.entity.ProductStatus;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.IProductQueryRepository;
import com.challenge.ecommercebackend.shared.domain.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetProductsUseCaseImpl implements GetProductsUseCase {

    private final IProductQueryRepository productQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllActive() {
        return productQueryRepository.findAllActive();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getActiveByCategoryId(Long categoryId) {
        return productQueryRepository.findAllByCategoryId(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getActiveByName(String name) {
        return productQueryRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productQueryRepository.findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<Product> getAllPaginated(int page, int size, String status, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> productPage;

        if (status != null && search != null) {
            productPage = productQueryRepository.findByStatusAndNameContainingIgnoreCase(
                    ProductStatus.valueOf(status), search, pageable);
        } else if (status != null) {
            productPage = productQueryRepository.findByStatus(ProductStatus.valueOf(status), pageable);
        } else if (search != null) {
            productPage = productQueryRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            productPage = productQueryRepository.findAll(pageable);
        }

        return PageResponse.<Product>builder()
                .content(productPage.getContent())
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .first(productPage.isFirst())
                .last(productPage.isLast())
                .hasNext(productPage.hasNext())
                .hasPrevious(productPage.hasPrevious())
                .build();
    }
}

