package com.challenge.ecommercebackend.modules.admin.domain.service;

import com.challenge.ecommercebackend.modules.admin.domain.usecase.GetDashboardStatsUseCase;
import com.challenge.ecommercebackend.modules.admin.domain.usecase.GetRecentOrdersUseCase;
import com.challenge.ecommercebackend.modules.admin.domain.usecase.GetTopSellingProductsUseCase;

/**
 * Servicio que agrupa todos los use cases de administración.
 */
public interface IAdminService {

    GetDashboardStatsUseCase getGetDashboardStatsUseCase();
    GetTopSellingProductsUseCase getGetTopSellingProductsUseCase();
    GetRecentOrdersUseCase getGetRecentOrdersUseCase();
}

