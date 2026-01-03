package com.challenge.ecommercebackend.api.dto.response;

public class GraphQLResponse<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
