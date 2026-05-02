package com.cinemaroyale.exceptions;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String msg) {
        super(msg);
    }
}