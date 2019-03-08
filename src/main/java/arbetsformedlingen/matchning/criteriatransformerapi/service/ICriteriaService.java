package arbetsformedlingen.matchning.criteriatransformerapi.service;

import arbetsformedlingen.matchning.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.matchning.criteriatransformerapi.elisecriteria.MatchningsparametrarDTO;
import reactor.core.publisher.Mono;

public interface ICriteriaService {
    Mono<Criteria> transformToCriteria(MatchningsparametrarDTO param);
    Mono<String> transformToQueryPath(MatchningsparametrarDTO param);
}
