package com.challenge.ecommercebackend.modules.user.persisten.repository.command;

import com.challenge.ecommercebackend.modules.user.persisten.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleCommandRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}

