package arbetsformedlingen.criteriatransformerapi.elisecriteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static arbetsformedlingen.criteriatransformerapi.elisecriteria.Constants.STOCKHOLM_TIMEZONE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchningsparametrarDTO {
    @JsonFormat(timezone = STOCKHOLM_TIMEZONE)
    protected Date franPubliceringsdatum;
    @JsonFormat(timezone = STOCKHOLM_TIMEZONE)
    protected Date tillPubliceringsdatum;
    protected Integer startrad;
    protected Integer maxAntal;
    protected String sorteringsordning;
    protected MatchningsprofilDTO matchningsprofil;
}
