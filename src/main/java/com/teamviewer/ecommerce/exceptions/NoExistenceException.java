package com.teamviewer.ecommerce.exceptions;

public class NoExistenceException extends IllegalArgumentException {

    public NoExistenceException(String entity) {
        super(String.format("%s does not exist.", entity));
    }
}
