package arbetsformedlingen.matchning.criteriatransformerapi.service;

import arbetsformedlingen.matchning.criteriatransformerapi.criteria.Criteria;
import reactor.core.publisher.Mono;

public interface IQueryCreatorService {
    Mono<String> createQueryParamFor(Criteria criteria);
}
