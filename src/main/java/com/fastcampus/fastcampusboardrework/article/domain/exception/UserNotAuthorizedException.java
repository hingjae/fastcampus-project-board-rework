package com.fastcampus.fastcampusboardrework.article.domain.exception;

public class UserNotAuthorizedException extends RuntimeException {

    private final static String ERROR_MESSAGE = "User Not Authorized";

    public UserNotAuthorizedException() {
        super(ERROR_MESSAGE);
    }

    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
