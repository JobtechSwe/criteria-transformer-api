
package arbetsformedlingen.criteriatransformerapi.exception;

import arbetsformedlingen.criteriatransformerapi.model.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorAttributes.class);

    public GlobalErrorAttributes() {
        super(false);
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(request, includeStackTrace);
        Throwable error = getError(request);
        if (error instanceof ApiException) {
            LOGGER.error("Caught an instance of: {}, error: {}", ApiException.class, error);
            HttpStatus status = ((ApiException) error).getStatus();
            map.put("status", status.value());
            map.put("error", status.getReasonPhrase());
        }

        return map;
    }

}
