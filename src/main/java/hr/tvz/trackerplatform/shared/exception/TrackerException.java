package hr.tvz.trackerplatform.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class TrackerException extends RuntimeException {

    private final ErrorMessage errorMessage;

    @Override
    public String getMessage() {
        return errorMessage.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return errorMessage.getHttpStatus();
    }
}
