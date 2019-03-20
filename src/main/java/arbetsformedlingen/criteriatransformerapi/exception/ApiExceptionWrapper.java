package arbetsformedlingen.criteriatransformerapi.exception;

import arbetsformedlingen.criteriatransformerapi.model.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Slf4j
@Component
class ApiExceptionWrapper extends DefaultErrorAttributes {

    ApiExceptionWrapper() {
        super(false);
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Throwable error = getError(request);
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, false);
        if (error instanceof ApiException) {
            log.debug("Caught an instance of: {}, err: {}", ApiException.class, error);
            HttpStatus status = ((ApiException) error).getStatus();
            errorAttributes.replace(ErrorAttribute.STATUS.value, status.value());
            errorAttributes.replace(ErrorAttribute.ERROR.value, status.getReasonPhrase());
            return errorAttributes;
        }

        return errorAttributes;
    }


    enum ErrorAttribute {
        STATUS("status"),
        ERROR("error");

        private final String value;

        ErrorAttribute(String value) {
            this.value = value;
        }
    }

}
