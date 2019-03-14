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
@Api(value = "/transform", description = "Transform elise criteria to sok-api criteria.")
public class CriteriaController {

    @Autowired
    private ICriteriaService service;

    @ApiOperation(value = "transform elise criteria to sok-api criteria")
    @RequestMapping(value = "/transform", method = RequestMethod.POST, produces = "application/json")
    public Mono<Criteria> translateToSokApiCriteria(@RequestBody MatchningsparametrarDTO dto) {
        return service.transformToCriteria(dto);
    }

    @ApiOperation(value = "transform elise criteria to sok-api queryPath")
    @RequestMapping(value = "/transform/querypath", method = RequestMethod.POST)
    public Mono<String> translateToQueryPath(@RequestBody MatchningsparametrarDTO dto) {
        return service.transformToQueryPath(dto);
    }

}
