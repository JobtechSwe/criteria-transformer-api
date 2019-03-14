package arbetsformedlingen.criteriatransformerapi.elisecriteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchningsprofilDTO {
    private List<ProfilKriteriumDTO> profilkriterier = new ArrayList<>();
    private List<SearchPackage> searchPackages;
}
