package com.challenge.ecommercebackend.modules.user.web.controller;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserResolver {

    @SchemaMapping(typeName = "User", field = "role")
    public String getRole(User user) {
        return user.getRole() != null ? user.getRole().getName() : null;
    }
}

