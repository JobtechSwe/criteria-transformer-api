package arbetsformedlingen.criteriatransformerapi.service;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.MatchningsparametrarDTO;
import reactor.core.publisher.Mono;

public interface ICriteriaService {
    Mono<Criteria> transformToCriteria(MatchningsparametrarDTO param);
    Mono<String> transformToQueryPath(MatchningsparametrarDTO param);
}
