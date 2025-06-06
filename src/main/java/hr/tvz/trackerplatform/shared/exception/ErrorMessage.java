package hr.tvz.trackerplatform.shared.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    DAILY_CHECK_NOT_FOUND("Daily check not found", HttpStatus.BAD_REQUEST),
    DAILY_CHECK_ALREADY_SUBMITTED("Daily check already submitted", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
