package com.brujua.comics.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@SuppressWarnings("unused")
@ControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger( MainExceptionHandler.class );

    @ExceptionHandler(InvalidCharacterException.class)
    protected ResponseEntity<String> handleInvalidCharacter(InvalidCharacterException ex) {
        log.info("Invalid request with charId {}", ex.getInput());

        String body = "Provided invalid character";
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MarvelServiceException.class)
    protected ResponseEntity<String> handleMarvelServiceError(MarvelServiceException ex) {
        log.error("Problems with marvel service", ex);

        String body = "Information unavailable, please try again later";
        return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleUnknownException(Exception ex) {
        log.error("Unknown exception", ex);

        String body = "Internal error";
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
