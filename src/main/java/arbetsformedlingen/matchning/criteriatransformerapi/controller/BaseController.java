package arbetsformedlingen.matchning.criteriatransformerapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @Value( "${loader.io.token}" )
    private String loaderIOToken;

    @RequestMapping(value = "/${loader.io.token}.txt", method = RequestMethod.GET, produces = "text/plain")
    public String getLoaderIOToken() {
        return loaderIOToken;
    }
}
