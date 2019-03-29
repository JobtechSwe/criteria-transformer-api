package arbetsformedlingen.criteriatransformerapi.exception;

import arbetsformedlingen.criteriatransformerapi.model.ApiException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class QueryCreationException extends ApiException {
    public QueryCreationException(String message, Throwable cause) {
        super(BAD_REQUEST, message, cause);
    }
}
