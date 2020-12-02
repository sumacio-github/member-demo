package io.sumac.demo.member;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public abstract class ApplicationException extends IllegalArgumentException {

    private final HttpStatus httpStatus = HttpStatus.CONFLICT;
    private final Instant timestamp = Instant.now();

    public ApplicationException(String msg) {
        super(msg);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public static ApplicationException duplicateLogin(String login) {
        return new DuplicationConstraintViolationException(String.format("A record with login '%s' already exists", login));
    }

    public static class DuplicationConstraintViolationException extends ApplicationException {
        private DuplicationConstraintViolationException(String msg) {
            super(msg);
        }
    }

}
