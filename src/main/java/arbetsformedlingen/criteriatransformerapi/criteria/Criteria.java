package arbetsformedlingen.criteriatransformerapi.criteria;

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
public class Criteria {
    private String q;
    private Integer offset;
    private Integer limit;
    private String sort;
    private List<GeographicCoordinate> geographicCoordinates = new ArrayList<>();
    private String publishedBefore;
    private String publishedAfter;
    private Boolean experience;
    private List<String> municipality = new ArrayList<>();
    private List<String> region = new ArrayList<>();
    private List<String> country = new ArrayList<>();
    private List<String> skill = new ArrayList<>();
    private List<String> field = new ArrayList<>();
    private List<String> extent = new ArrayList<>();
    private List<String> drivinglicence = new ArrayList<>();
    private List<String> employmenttype = new ArrayList<>();
    private List<String> group = new ArrayList<>();
    private List<String> occupation = new ArrayList<>();
    private Parttime parttime;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeographicCoordinate {
        private String latitude;
        private String longitude;
        private String radius;
    }
}
