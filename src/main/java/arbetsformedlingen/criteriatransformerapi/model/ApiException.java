package arbetsformedlingen.criteriatransformerapi.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString(callSuper = true)
public abstract class ApiException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public ApiException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

}