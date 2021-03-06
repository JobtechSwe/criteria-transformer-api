package arbetsformedlingen.criteriatransformerapi.service;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.criteria.Parttime;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.KriteriumegenskapDTO;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.MatchningsparametrarDTO;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.ProfilKriteriumDTO;
import arbetsformedlingen.criteriatransformerapi.exception.ContentNotAllowedException;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static arbetsformedlingen.criteriatransformerapi.elisecriteria.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class TransformerServiceTest {

    private TransformerService service;

    @Before
    public void setUp() {
        service = new TransformerService();
    }

    @Test
    public void shouldHandleExceptionWhenTransformingFails() {
        //Given:
        TransformerService spy = spy(TransformerService.class);

        doThrow(RuntimeException.class).when(spy).transform(any(MatchningsparametrarDTO.class));

        try {
            //When:
            spy.transformToCriteria(new MatchningsparametrarDTO());
        } catch (Exception e) {
            //Then:
            assertThat(e.getCause()).isInstanceOf(ContentNotAllowedException.class);
        }
    }

    @Test
    public void shouldFormatDate() {
        //Given:
        String expectedValue = "2012-03-05T00:00:00";

        //When:
        String formattedDate = service.populateDate(getCalendar().getTime());

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
    public void shouldRemoveStarStarFromFritext() {
        //Given:
        Criteria criteria = new Criteria();

        //When:
        service.populateFritext(criteria ,"**");

        //Then:
        assertThat(criteria.getQ()).isEmpty();
    }

    @Test
    public void shouldAddAnstallningstypFullTime() {
        //Given:
        Criteria fullTime = new Criteria();
        Criteria summerJob = new Criteria();
        Criteria behov = new Criteria();
        Criteria foregin = new Criteria();

        //When:
        service.addAnstallningstyp(fullTime, VANLIG_ANSTALLNING);
        service.addAnstallningstyp(summerJob, SUMMER_JOB);
        service.addAnstallningstyp(behov, BEHOVSANSTALLNING);
        service.addAnstallningstyp(foregin, FOREGIN);

        //Then:
        assertThat(fullTime.getEmploymenttype()).containsExactly(VANLIG_ANSTALLNING);
        assertThat(summerJob.getEmploymenttype()).containsExactly(SUMMER_JOB);
        assertThat(behov.getEmploymenttype()).containsExactly(BEHOVSANSTALLNING);
        assertThat(foregin.getEmploymenttype()).containsExactly(FOREGIN);
    }

    private Calendar getCalendar() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Stockholm"));
        cal.clear();
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 2);
        cal.set(Calendar.DATE, 5);

        return cal;
    }

}