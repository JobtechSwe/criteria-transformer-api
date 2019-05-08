package arbetsformedlingen.criteriatransformerapi.elisecriteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchningsparametrarDTO {
    protected Date franPubliceringsdatum;
    protected Date tillPubliceringsdatum;
    protected Integer startrad;
    protected Integer maxAntal;
    protected String sorteringsordning;
    protected MatchningsprofilDTO matchningsprofil;
}
