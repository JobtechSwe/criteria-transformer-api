package arbetsformedlingen.criteriatransformerapi.service;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.MatchningsparametrarDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CriteriaServiceTest {

    private CriteriaService criteriaService;

    @Mock
    private ITransformerService transformerService;

    @Mock
    private IQueryCreatorService queryCreatorService;

    @Before
    public void setUp() {
        criteriaService = new CriteriaService(transformerService, queryCreatorService);
    }

    @Test
    public void transformToCriteria() {
        //Given:
        MatchningsparametrarDTO param = MatchningsparametrarDTO.builder().build();
        when(transformerService.transformToCriteria(param)).thenReturn(Mono.empty());

        //When:
        Mono<Criteria> criteriaMono = criteriaService.transformToCriteria(param);

        //Then:
        assertThat(criteriaMono).isNotNull();
    }

    @Test
    public void transformToQueryPath() {
    }
}