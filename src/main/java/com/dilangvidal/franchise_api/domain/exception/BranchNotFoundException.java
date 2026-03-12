package com.dilangvidal.franchise_api.domain.exception;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String branchId) {
        super("Sucursal no encontrada, id: " + branchId);
    }
}
