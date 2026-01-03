package com.challenge.ecommercebackend.api;

import com.challenge.ecommercebackend.modules.user.persisten.entity.UserStatus;
import com.challenge.ecommercebackend.modules.user.persisten.repository.query.IUserQueryRepository;
import org.springframework.stereotype.Component;

@Component
public class UserExternalApiImpl implements IUserExternalApi {

    private final IUserQueryRepository userQueryRepository;

    public UserExternalApiImpl(IUserQueryRepository userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }

    @Override
    public long countActiveUsers() {
        return userQueryRepository.countByStatus(UserStatus.ACTIVE);
    }
}

