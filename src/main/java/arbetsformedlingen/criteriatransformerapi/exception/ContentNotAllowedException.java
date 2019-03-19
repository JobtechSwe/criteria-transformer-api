package arbetsformedlingen.criteriatransformerapi.exception;

import arbetsformedlingen.criteriatransformerapi.model.ApiException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ContentNotAllowedException extends ApiException {
    public ContentNotAllowedException(String message) {
        super(BAD_REQUEST, message);
    }

    public ContentNotAllowedException(String message, Throwable cause) {
        super(BAD_REQUEST, message, cause);
    }

}
