package com.dilangvidal.franchise_api.domain.exception;

public class DuplicateNameException extends RuntimeException {
    public DuplicateNameException(String entity, String name) {
        super(entity + " ya existe con el nombre: " + name);
    }
}
