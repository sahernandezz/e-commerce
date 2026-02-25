package com.challenge.ecommercebackend.modules.user.persisten.repository.command;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserCommandRepository extends JpaRepository<User, Long> {

}

