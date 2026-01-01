package com.challenge.ecommercebackend.modules.product.service;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.persisten.repository.command.IProductCommandRepository;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.IProductQueryRepository;
import com.challenge.ecommercebackend.modules.product.web.dto.request.InputProductRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    private final IProductCommandRepository productCommandRepository;
    private final IProductQueryRepository productQueryRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(IProductCommandRepository productCommandRepository,
                             IProductQueryRepository productQueryRepository,
                             ModelMapper modelMapper) {
        this.productCommandRepository = productCommandRepository;
        this.productQueryRepository = productQueryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Product> getAllProducts(Pageable pageable) {
        return productQueryRepository.findAllActive();
    }

    @Override
    public List<Product> getAllProductsActive() {
        return productQueryRepository.findAllActive();
    }

    @Override
    public List<Product> getAllProductsActiveByCategoryId(Long categoryId) {
        return productQueryRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public Product addProduct(InputProductRequest inputProductRequest) {
        Product product = modelMapper.map(inputProductRequest, Product.class);
        product.setCreatedAt(new Date());
        return productCommandRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productQueryRepository.findActiveById(id);
    }

    @Override
    public Optional<Product> updateProduct(Product product) {
        Optional<Product> productOptional = productCommandRepository.findById(product.getId());
        if (productOptional.isPresent()) {
            product.setUpdatedAt(new Date());
            return Optional.of(productCommandRepository.save(product));
        }
        return Optional.empty();
    }

    @Override
    public List<Product> getAllProductsActiveByName(String name) {
        return productQueryRepository.findAllByNameContainingIgnoreCase(name);
    }
}

