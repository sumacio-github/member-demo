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
        return new DuplicationConstraintViolationException(String.format("A resource with login '%s' already exists", login));
    }

    public static ApplicationException idMismatch(int urlId, int bodyId) {
        return new UneditableFieldException(String.format("The 'id' %s cannot be changed to %s", urlId, bodyId));
    }

    public static ApplicationException createFailed() {
        return new UnprocessableRequestException("A resource was not created");
    }

    public static ApplicationException notFound() {
        return new NotFoundException("Resource not found");
    }

    public static class DuplicationConstraintViolationException extends ApplicationException {
        private DuplicationConstraintViolationException(String msg) {
            super(msg);
        }
    }

    public static class UneditableFieldException extends ApplicationException {
        private UneditableFieldException(String msg) {
            super(msg);
        }
    }

    public static class UnprocessableRequestException extends ApplicationException {
        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }
        private UnprocessableRequestException(String msg) {
            super(msg);
        }
    }

    public static class NotFoundException extends ApplicationException {
        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.NOT_FOUND;
        }
        private NotFoundException(String msg) {
            super(msg);
        }
    }

}
