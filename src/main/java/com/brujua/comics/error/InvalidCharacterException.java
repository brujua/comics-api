package com.brujua.comics.error;

public class InvalidCharacterException extends RuntimeException {

    private final String input;

    public InvalidCharacterException(String input) {
        super();
        this.input = input;
    }

    public String getInput() {
        return input;
    }
}
