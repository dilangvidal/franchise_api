package com.dilangvidal.franchise_api.domain.exception;

public class FranchiseNotFoundException extends RuntimeException {
    public FranchiseNotFoundException(String franchiseId) {
        super("Franquicia no encontrada, id: " + franchiseId);
    }
}
