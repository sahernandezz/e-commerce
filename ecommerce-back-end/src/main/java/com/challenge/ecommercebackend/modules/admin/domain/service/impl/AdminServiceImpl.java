package com.challenge.ecommercebackend.modules.admin.domain.service.impl;

import com.challenge.ecommercebackend.modules.admin.domain.service.IAdminService;
import com.challenge.ecommercebackend.modules.admin.domain.usecase.GetDashboardStatsUseCase;
import com.challenge.ecommercebackend.modules.admin.domain.usecase.GetRecentOrdersUseCase;
import com.challenge.ecommercebackend.modules.admin.domain.usecase.GetTopSellingProductsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio que agrupa todos los use cases de administración.
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements IAdminService {

    private final GetDashboardStatsUseCase getDashboardStatsUseCase;
    private final GetTopSellingProductsUseCase getTopSellingProductsUseCase;
    private final GetRecentOrdersUseCase getRecentOrdersUseCase;

    @Override
    public GetDashboardStatsUseCase getGetDashboardStatsUseCase() {
        return getDashboardStatsUseCase;
    }

    @Override
    public GetTopSellingProductsUseCase getGetTopSellingProductsUseCase() {
        return getTopSellingProductsUseCase;
    }

    @Override
    public GetRecentOrdersUseCase getGetRecentOrdersUseCase() {
        return getRecentOrdersUseCase;
    }
}

