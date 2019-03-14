package arbetsformedlingen.criteriatransformerapi.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Parttime {
    private Integer min;
    private Integer max;
}
