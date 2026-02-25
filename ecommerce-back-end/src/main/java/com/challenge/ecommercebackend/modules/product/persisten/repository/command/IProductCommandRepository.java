package com.challenge.ecommercebackend.modules.product.persisten.repository.command;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductCommandRepository extends JpaRepository<Product, Long> {

}

