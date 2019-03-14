package arbetsformedlingen.criteriatransformerapi.service;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import reactor.core.publisher.Mono;

public interface IQueryCreatorService {
    Mono<String> createQueryParamFor(Criteria criteria);
}
