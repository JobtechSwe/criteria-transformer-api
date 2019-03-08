package arbetsformedlingen.matchning.criteriatransformerapi.service;

import arbetsformedlingen.matchning.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.matchning.criteriatransformerapi.elisecriteria.MatchningsparametrarDTO;
import reactor.core.publisher.Mono;

public interface ITransformerService {
    Mono<Criteria> transformToCriteria(MatchningsparametrarDTO matchningsparametrar);
}
