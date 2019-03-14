package arbetsformedlingen.criteriatransformerapi.controller;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.MatchningsparametrarDTO;
import arbetsformedlingen.criteriatransformerapi.service.ICriteriaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Api(value = "/transform", description = "Transform elise criteria to elastic criteria.")
public class CriteriaController {

    @Autowired
    private ICriteriaService service;

    @ApiOperation(value = "Transform elise criteria message to a Criteria for sok-api")
    @RequestMapping(value = "/transform", method = RequestMethod.POST, produces = "application/json")
    public Mono<Criteria> translateToSokApiCriteria(@RequestBody MatchningsparametrarDTO dto) {
        return service.transformToCriteria(dto);
    }

    @ApiOperation(value = "Transform elise criteria message to a queryPath for sok-api")
    @RequestMapping(value = "/transform/querypath", method = RequestMethod.POST)
    public Mono<String> translateToQueryPath(@RequestBody MatchningsparametrarDTO dto) {
        return service.transformToQueryPath(dto);
    }

}
