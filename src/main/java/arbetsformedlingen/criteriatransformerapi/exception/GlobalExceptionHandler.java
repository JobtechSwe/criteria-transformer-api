package arbetsformedlingen.criteriatransformerapi.exception;


import arbetsformedlingen.criteriatransformerapi.model.ApiError;
import arbetsformedlingen.criteriatransformerapi.model.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import static java.lang.System.currentTimeMillis;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ContentNotAllowedException.class, QueryCreationException.class})
    public final ResponseEntity<ApiError> handleApiException(ApiException exception, ServerWebExchange exchange) {
        LOGGER.debug("ApiException caught. Status: {}, message: {}", exception.getStatus(), exception.getMessage());
        return createApiError(exception.getStatus(), exchange.getRequest(), exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException exception, ServerWebExchange exchange) {
        LOGGER.warn("Runtime exception caught. message: {}", exception.getMessage());
        return createApiError(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getRequest(), exception.getMessage());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ApiError> handleBindException(WebExchangeBindException exception, ServerWebExchange exchange) {
        LOGGER.warn("Binding failure. status:{}, request:{}, message:{}", exception.getStatus(), exchange.getRequest(), exception.getMessage());
        return createApiError(exception.getStatus(), exchange.getRequest(), exception.getMessage());
    }

    private ResponseEntity<ApiError> createApiError(HttpStatus status, ServerHttpRequest request, String message) {
        return new ResponseEntity<>(new ApiError(
                currentTimeMillis(),
                request.getPath().value(),
                status.value(),
                status.getReasonPhrase(),
                message
        ), status);
    }
}
