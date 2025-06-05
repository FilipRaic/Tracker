package hr.tvz.trackerplatform.shared.exception;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
@AllArgsConstructor
public class TrackerException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public String getErrorMessage() {
        return errorMessage.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return errorMessage.getHttpStatus();
    }
}