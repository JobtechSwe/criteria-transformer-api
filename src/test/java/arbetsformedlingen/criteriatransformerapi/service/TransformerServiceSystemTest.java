package arbetsformedlingen.criteriatransformerapi.service;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.criteria.Criteria.GeographicCoordinate;
import arbetsformedlingen.criteriatransformerapi.criteria.CriteriaTypeValue;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.MatchningsparametrarDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TransformerServiceSystemTest {

    private ITransformerService service;

    @Before
    public void setUp() {
        service = new TransformerService();
    }

    @Test
    public void transformIngenErfarenhetToCriteria() throws IOException {
        //Given:
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/IngenErfarenhet.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getExperience()).isFalse();
    }

    @Test
    public void transformYrkesOmradeToCriteria() throws IOException {
        //Given:
        String expected = "3";
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/yrkesomrade.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        String yrkesomrade = criteria.getField().get(0);
        assertThat(yrkesomrade).isEqualTo(expected);
    }

    @Test
    public void transformYrkesGruppToCriteria() throws IOException {
        //Given:
        String expected = "3513";
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/yrkesgrupp.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        String grupp = criteria.getGroup().get(0);
        assertThat(grupp).isEqualTo(expected);
    }

    @Test
    public void transformYrkesRollToCriteria() throws IOException {
        //Given:
        String expected = "2494";
        String expected2 = "6794";
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/yrkesroll.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        List<String> occupation = criteria.getOccupation();
        assertThat(occupation.contains(expected)).isTrue();
        assertThat(occupation.contains(expected2)).isTrue();
    }

    @Test
    public void transformKommunToCriteria() throws IOException {
        //Given:
        String expected = "0180";

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/kommun.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        List<String> municipality = criteria.getMunicipality();
        assertThat(municipality.get(0)).isEqualTo(expected);
    }

    @Test
    public void transformLanToCriteria() throws IOException {
        //Given:
        String expected = "14";

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/lan.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        List<String> region = criteria.getRegion();
        assertThat(region.get(0)).isEqualTo(expected);
    }

    @Test
    public void transformGeoAdressToCriteria() throws IOException {
        //Given:
        GeographicCoordinate coordinate = GeographicCoordinate.builder().latitude("59.3408384265908").longitude("18.0474815522544").radius("5").build();

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/geoadress.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        List<GeographicCoordinate> geographicCoordinates = criteria.getGeographicCoordinates();
        assertThat(geographicCoordinates.size()).isEqualTo(1);
        assertThat(geographicCoordinates).contains(coordinate);
    }

    @Test
    public void transformMultipleGeoAdressToCriteria() throws IOException {
        //Given:
        GeographicCoordinate skhlm = GeographicCoordinate.builder().latitude("59.2992259616122").longitude("17.9363485424389").radius("5").build();
        GeographicCoordinate alvsjo = GeographicCoordinate.builder().latitude("59.28048055076").longitude("18.0034328607383").radius("5").build();

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/multiple-geo-address.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        List<GeographicCoordinate> geographicCoordinates = criteria.getGeographicCoordinates();
        assertThat(geographicCoordinates.size()).isEqualTo(2);
        assertThat(geographicCoordinates).contains(skhlm).contains(alvsjo);
    }

    @Test
    public void transformOspecificeradArbetsortToCriteria() throws IOException {
        //Given:
        String expected = "90";

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/ospecificerad-arbetsort.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getRegion().get(0)).isEqualTo(expected);
    }

    @Test
    public void transformFritextToCriteria() throws IOException {
        //Given:
        String expected = "SQL";

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/fritext-annonstext.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getQ()).isEqualTo(expected);
    }

    @Test
    public void transformMultipleFritextToCriteria() throws IOException {
        //Given:
        String expected = "java sql";

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/multiple-fritext.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getQ()).isEqualTo(expected);
    }

    @Test
    public void transformYrkeRollFritext() throws IOException {
        //Given:
        String expectedFritext = "-sql";

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/yrkesroll-fritext-negativKriterie.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getQ()).isEqualTo(expectedFritext);
        assertThat(criteria.getOccupation()).contains("6826");
    }

    @Test
    public void transformVanligAnstallningToCriteria() throws IOException {
        //Given:
        String expected = "1";

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/vanlig-anstallning.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getEmploymenttype()).contains(expected);
    }

    @Test
    public void transformSommarjobbToCriteria() throws IOException {
        //Given:
        String expected = "2";

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/sommarjobb.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getEmploymenttype()).contains(expected);
    }

    @Test
    public void transformBehovsanstallningToCriteria() throws IOException {
        //Given:
        String expected = "4";

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/behovsanstallning.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getEmploymenttype()).contains(expected);
    }

    @Test
    public void transformUtlandJobbToCriteria() throws IOException {
        //Given:
        String expected = "3";

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/utlandsJobb.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getEmploymenttype()).contains(expected);
    }

    @Test
    public void transformEndastHeltidToCriteria() throws IOException {
        //Given:
        String expected = "1";

        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/endast-heltid.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getExtent()).contains(expected);
    }

    @Test
    public void transformWhenArbetsomfattningIsNull() throws IOException {
        //Given:
        MatchningsparametrarDTO params = getParamsFor("test-data/arbets-omfanning-null.json");

        //When:
        Criteria criteria = service.transformToCriteria(params).block();

        assertThat(criteria).isNotNull();
    }


    @Test
    public void transformDeltidToCriteria() throws IOException {
        //Given:
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/deltid.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getParttime().getMin()).isEqualTo(5);
        assertThat(criteria.getParttime().getMax()).isEqualTo(95);
    }

    @Test
    public void transformHeltidDeltidToCriteria() throws IOException {
        //Given:
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/heltid-deltid.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getParttime().getMin()).isEqualTo(5);
        assertThat(criteria.getParttime().getMax()).isEqualTo(100);
        assertThat(criteria.getExtent()).containsExactly("1", "2");
    }

    @Test
    public void transformKompetensToCriteria() throws IOException {
        //Given:
        String expectedSkill = "5572";
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/kompetens.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getSkill()).containsExactly(expectedSkill);
    }

    @Test
    public void transformKorkortToCriteria() throws IOException {
        //Given:
        String expected = "18";
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/korkort.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getDrivinglicence()).containsExactly(expected);
    }

    @Test
    public void transformYrkesrollSorteringSistaansokningsdatum() throws IOException {
        //Given:
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/yrkesroll-sortering-sistaansokningsdatum.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getSort()).isEqualTo(CriteriaTypeValue.APPLYDATE_ASC);
        assertThat(criteria.getOccupation()).containsExactly("7633");
    }

    @Test
    public void transformYrkesrollFilterDatumToCriteria() throws IOException {
        //Given:
        String expected = "7633";
        String expectedDate = "2018-11-05T00:00:00";
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/yrkesroll-filtrering-datum.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getSort()).isEqualTo(CriteriaTypeValue.RELEVANCE);
        assertThat(criteria.getOccupation()).containsExactly(expected);

        String publishedAfter = criteria.getPublishedAfter();
        assertThat(publishedAfter).isEqualTo(expectedDate);
    }

    @Test
    public void transformSokpaketToCriteria() throws IOException {
        //Given:
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/sokpaket.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getSort()).isEqualTo(CriteriaTypeValue.RELEVANCE);
        assertThat(criteria.getLimit()).isEqualTo("25");
        assertThat(criteria.getGroup()).containsExactly("5132", "5131", "9413", "5120", "3451", "9411", "9412", "1720");
    }

    @Test
    public void transformOffsetToCriteria() throws IOException {
        //Given:
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/paginering.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getOffset()).isEqualTo("100");
    }

    @Test
    public void transformYrkeKommunToCriteria() throws IOException {
        //Given:
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/yrke-kommun.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getOccupation()).containsExactly("6826");
        assertThat(criteria.getMunicipality()).containsExactly("0180");
    }

    @Test
    public void transformYrkesgruppOscpecificerArbetsortToCriteria() throws IOException {
        //Given:
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/yrkesgrupp-ospec-arbetsort.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getGroup()).containsExactly("2514");
        assertThat(criteria.getRegion()).containsExactly("90");
    }


    @Test
    public void transformBigComboToCriteria() throws IOException {
        //Given:
        MatchningsparametrarDTO matchningsparametrar = getParamsFor("test-data/big-combo.json");

        //When:
        Criteria criteria = service.transformToCriteria(matchningsparametrar).block();

        //Then:
        assertThat(criteria.getQ()).isEqualTo("sql");
        assertThat(criteria.getOffset()).isEqualTo("0");
        assertThat(criteria.getLimit()).isEqualTo("25");
        assertThat(criteria.getSort()).isEqualTo(CriteriaTypeValue.RELEVANCE);

        List<GeographicCoordinate> geographicCoordinates = criteria.getGeographicCoordinates();
        assertThat(geographicCoordinates).isEmpty();

        assertThat(criteria.getPublishedBefore()).isNull();
        assertThat(criteria.getPublishedAfter()).isNull();
        assertThat(criteria.getExperience()).isNull();
        assertThat(criteria.getMunicipality()).containsExactly("1415");
        assertThat(criteria.getRegion()).containsExactly("14");
        assertThat(criteria.getCountry()).containsExactly("155");
        assertThat(criteria.getSkill()).containsExactly("5572");
        assertThat(criteria.getField()).containsExactly("3");
        assertThat(criteria.getExtent()).containsExactly("1", "2");
        assertThat(criteria.getDrivinglicence()).containsExactly("6");
        assertThat(criteria.getEmploymenttype()).containsExactly("4", "2", "1");
        assertThat(criteria.getGroup()).containsExactly("2611");
        assertThat(criteria.getOccupation()).containsExactly("6826", "2079");
        assertThat(criteria.getParttime().getMin()).isEqualTo(5);
        assertThat(criteria.getParttime().getMax()).isEqualTo(100);
    }

    private MatchningsparametrarDTO getParamsFor(String filePath) throws IOException {
        File file = new ClassPathResource(filePath).getFile();
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // Hack time module to allow 'Z' at the end of string (i.e. javascript json's)
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        mapper.registerModule(javaTimeModule);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return mapper.readValue(file, MatchningsparametrarDTO.class);
    }
}