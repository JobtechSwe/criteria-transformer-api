package arbetsformedlingen.criteriatransformerapi.service;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.criteria.Parttime;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.KriteriumegenskapDTO;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.ProfilKriteriumDTO;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;

import static arbetsformedlingen.criteriatransformerapi.criteria.CriteriaTypeValue.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TransformerServiceTest {

    private TransformerService service;

    @Before
    public void setUp() throws Exception {
        service = new TransformerService();
    }

    @Test
    public void shouldFormatDate() {
        //Given:
        String expectedValue = "2012-02-05T00:00:00";

        //When:
        String formattedDate = service.populateDate(getCalendar());

        //Then:
        assertThat(formattedDate).isEqualTo(expectedValue);
    }

    @Test
    public void shouldNotPopulateParttime() {
        //When:
        Parttime parttime = service.populateParttime(new ProfilKriteriumDTO());

        assertThat(parttime.getMin()).isNull();
        assertThat(parttime.getMax()).isNull();
    }

    @Test
    public void shouldPopulateParttime() {
        //When:
        KriteriumegenskapDTO egenskapMin = new KriteriumegenskapDTO();
        egenskapMin.setTyp("ARBETSOMFATTNING_MIN");
        egenskapMin.setVarde("50");

        KriteriumegenskapDTO egenskapMax = new KriteriumegenskapDTO();
        egenskapMax.setTyp("ARBETSOMFATTNING_MAX");
        egenskapMax.setVarde("100");

        ProfilKriteriumDTO kriteriumDTO = new ProfilKriteriumDTO();
        kriteriumDTO.getEgenskaper().add(egenskapMin);
        kriteriumDTO.getEgenskaper().add(egenskapMax);


        Parttime parttime = service.populateParttime(kriteriumDTO);

        assertThat(parttime.getMin()).isEqualTo(50);
        assertThat(parttime.getMax()).isEqualTo(100);
    }

    @Test
    public void shouldReturnNullWhenArgNullWhenFormatDate() {
        //When:
        String formattedDate = service.populateDate(null);

        //Then:
        assertThat(formattedDate).isEqualTo(null);
    }

    @Test
    public void shouldPopulateFritext() {
        //Given:
        String java = "java";
        Criteria criteria = new Criteria();

        //When:
        service.populateFritext(criteria, java);

        //Then:
        assertThat(criteria.getQ()).isEqualTo(java);
    }

    @Test
    public void shouldPopulateMoreInFritext() {
        //Given:
        String java = "java";
        String scrum = "scrum";

        Criteria criteria = new Criteria();
        criteria.setQ(scrum);

        //When:
        service.populateFritext(criteria, java);

        //Then:
        assertThat(criteria.getQ()).isEqualTo(scrum + " " + java);
    }

    @Test
    public void shouldNotPopulateFritext() {
        //Given:
        Criteria criteria = new Criteria();

        //When:
        service.populateFritext(criteria, "");

        //Then:
        assertThat(criteria.getQ()).isNull();
    }

    @Test
    public void shouldAddAnstallningstypFullTime() {
        //Given:
        Criteria fullTime = new Criteria();
        Criteria summerJob = new Criteria();
        Criteria behov = new Criteria();
        Criteria foregin = new Criteria();

        //When:
        service.addAnstallningstyp(fullTime, FULL_TIME);
        service.addAnstallningstyp(summerJob, SUMMER_JOB);
        service.addAnstallningstyp(behov, BEHOVSANSTALLNING);
        service.addAnstallningstyp(foregin, FOREGIN);

        //Then:
        assertThat(fullTime.getEmploymenttype()).containsExactly(FULL_TIME);
        assertThat(summerJob.getEmploymenttype()).containsExactly(SUMMER_JOB);
        assertThat(behov.getEmploymenttype()).containsExactly(BEHOVSANSTALLNING);
        assertThat(foregin.getEmploymenttype()).containsExactly(FOREGIN);
    }

    @Test
    public void shouldNotAddAnstallningstypFullTime() {
        //Given:
        Criteria unknown = new Criteria();

        //When:
        service.addAnstallningstyp(unknown, "190");

        //Then:
        assertThat(unknown.getEmploymenttype()).isEmpty();
    }

    private LocalDateTime getCalendar() {
        return LocalDateTime.of(2012, 2, 05, 00, 00, 00);
    }

}