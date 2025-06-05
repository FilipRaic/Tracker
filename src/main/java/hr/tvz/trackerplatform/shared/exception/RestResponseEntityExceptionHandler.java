package hr.tvz.trackerplatform.shared.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TrackerException.class)
    protected ResponseEntity<Object> handleException(TrackerException ex, WebRequest request) {
        return handleExceptionInternal(ex, buildExceptionResponse(ex), new HttpHeaders(), ex.getHttpStatus(), request);
    }

    private ExceptionResponse buildExceptionResponse(TrackerException ex) {
        return new ExceptionResponse(ex.getMessage(), ex.getHttpStatus());
    }
}
