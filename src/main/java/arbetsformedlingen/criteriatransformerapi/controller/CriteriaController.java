package arbetsformedlingen.criteriatransformerapi.controller;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.MatchningsparametrarDTO;
import arbetsformedlingen.criteriatransformerapi.service.ICriteriaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Api(value = "/transform")
public class CriteriaController {

    @Autowired
    private ICriteriaService service;

    @ApiOperation(value = "transform elise criteria to sok-api criteria")
    @PostMapping(value = "/transform", produces = "application/json")
    public Mono<Criteria> translateToSokApiCriteria(@RequestBody MatchningsparametrarDTO dto) {
        return service.transformToCriteria(dto);
    }

    @ApiOperation(value = "transform elise criteria to sok-api queryPath")
    @PostMapping(value = "/transform/querypath")
    public Mono<String> translateToQueryPath(@RequestBody MatchningsparametrarDTO dto) {
        return service.transformToQueryPath(dto);
    }

}
