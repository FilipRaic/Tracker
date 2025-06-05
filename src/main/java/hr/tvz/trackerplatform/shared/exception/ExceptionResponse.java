package hr.tvz.trackerplatform.shared.exception;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(String message, HttpStatus status) {
}
