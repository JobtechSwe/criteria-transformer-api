package arbetsformedlingen.criteriatransformerapi.elisecriteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchningsparametrarDTO {

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    protected LocalDateTime franPubliceringsdatum;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    protected LocalDateTime tillPubliceringsdatum;
    protected Integer startrad;
    protected Integer maxAntal;
    protected String sorteringsordning;
    protected MatchningsprofilDTO matchningsprofil;
}
