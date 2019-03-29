package arbetsformedlingen.criteriatransformerapi.service;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.criteria.Parttime;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryCreatorServiceTest {

    private QueryCreatorService service;

    @Before
    public void setUp() {
        service = new QueryCreatorService();
    }

    @Test
    public void shouldCreateQueryParams() {
        //Given:
        String expected = "?q=java&offset=0&limit=25&sort=relevance&published-before=2019-01-22T00:00:00";
        Criteria criteria = Criteria.builder()
                .q("java")
                .offset("0")
                .limit("25")
                .sort("relevance")
                .publishedBefore("2019-01-22T00:00:00")
                .build();

        //When:
        String queryParameter = service.createQueryParamFor(criteria).block();

        //Then:
        assertThat(queryParameter).isEqualTo(expected);
    }

    @Test
    public void shouldCreateQueryParams2() {
        //Given:
        String expected = "?q=java&offset=0&limit=50&sort=relevance&published-after=2019-01-22T00:00:00&experience=true&municipality=0180&municipality=0199&region=14";
        Criteria criteria = Criteria.builder()
                .q("java")
                .offset("0")
                .limit("50")
                .sort("relevance")
                .publishedAfter("2019-01-22T00:00:00")
                .experience(Boolean.TRUE)
                .municipality(Arrays.asList("0180", "0199"))
                .region(Collections.singletonList("14"))
                .build();

        //When:
        String queryParameter = service.createQueryParamFor(criteria).block();

        //Then:
        assertThat(queryParameter).isEqualTo(expected);
    }

    @Test
    public void shouldCreateQueryParams3() {
        //Given:
        String expected = "?q=java&offset=0&limit=50&field=3&skill=5572&skill=8181&extent=1&country=155";
        Criteria criteria = Criteria.builder()
                .q("java")
                .offset("0")
                .limit("50")
                .country(Collections.singletonList("155"))
                .skill(Arrays.asList("5572", "8181"))
                .field(Collections.singletonList("3"))
                .extent(Collections.singletonList("1"))
                .build();

        //When:
        String queryParameter = service.createQueryParamFor(criteria).block();

        //Then:
        assertThat(queryParameter).isEqualTo(expected);
    }

    @Test
    public void shouldCreateQueryParams4() {
        //Given:
        String expected = "?q=java&offset=0&limit=50&occupation=2494&occupation=1212&group=3513&parttime.min=5&parttime.max=95&drivinglicence=A&drivinglicence=B";
        Criteria criteria = Criteria.builder()
                .q("java")
                .offset("0")
                .limit("50")
                .drivinglicence(Arrays.asList("A", "B"))
                .group(Collections.singletonList("3513"))
                .occupation(Arrays.asList("2494", "1212"))
                .parttime(Parttime.builder().min(5).max(95).build())
                .build();

        //When:
        String queryParameter = service.createQueryParamFor(criteria).block();

        //Then:
        assertThat(queryParameter).isEqualTo(expected);
    }

}