package com.challenge.ecommercebackend.modules.product.persisten.repository.command;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryCommandRepository extends JpaRepository<Category, Long> {

}

