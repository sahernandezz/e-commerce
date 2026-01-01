package com.challenge.ecommercebackend.api;

import java.util.Map;

public interface IProductExternalApi {
    Map<String, Object> getProductById(Long id);
}
