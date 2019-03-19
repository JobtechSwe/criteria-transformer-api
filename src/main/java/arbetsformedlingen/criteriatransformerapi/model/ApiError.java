package arbetsformedlingen.criteriatransformerapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
    private Long timestamp;
    private String path;
    private Integer status;
    private String error;
    private String message;
}
