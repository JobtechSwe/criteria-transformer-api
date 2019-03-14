package arbetsformedlingen.criteriatransformerapi.elisecriteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfilKriteriumDTO {
    private String varde;
    private String typ;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<KriteriumegenskapDTO> egenskaper = new ArrayList<>();
}
