package arbetsformedlingen.criteriatransformerapi.service;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.MatchningsparametrarDTO;
import reactor.core.publisher.Mono;

public interface ITransformerService {
    Mono<Criteria> transformToCriteria(MatchningsparametrarDTO matchningsparametrar);
}
