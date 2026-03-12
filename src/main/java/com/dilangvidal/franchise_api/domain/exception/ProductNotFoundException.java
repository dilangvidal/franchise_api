package com.dilangvidal.franchise_api.domain.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productId) {
        super("Producto no encontrado, id: " + productId);
    }
}
