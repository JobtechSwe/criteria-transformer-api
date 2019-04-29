package arbetsformedlingen.criteriatransformerapi.service;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.MatchningsparametrarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CriteriaService implements ICriteriaService {

    private ITransformerService transformerService;
    private IQueryCreatorService queryCreatorService;

    @Autowired
    public CriteriaService(ITransformerService transformerService, IQueryCreatorService queryCreatorService) {
        this.transformerService = transformerService;
        this.queryCreatorService = queryCreatorService;
    }

    @Override
    public Mono<Criteria> transformToCriteria(MatchningsparametrarDTO param) {
        return transformerService.transformToCriteria(param);
    }

    @Override
    public Mono<String> transformToQueryPath(MatchningsparametrarDTO param) {
        return transformerService.transformToCriteria(param)
                .flatMap(criteria -> queryCreatorService.createQueryParamFor(criteria));
    }
}
