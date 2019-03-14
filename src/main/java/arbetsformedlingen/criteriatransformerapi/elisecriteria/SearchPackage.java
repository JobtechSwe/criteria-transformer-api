package arbetsformedlingen.criteriatransformerapi.elisecriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchPackage {
    private String key;
    private String title;
}
