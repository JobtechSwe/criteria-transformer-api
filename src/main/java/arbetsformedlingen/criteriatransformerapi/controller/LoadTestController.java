package arbetsformedlingen.criteriatransformerapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadTestController {

    @Value( "${loader.io.token}" )
    private String loaderIOToken;

    @GetMapping(value = "/${loader.io.token}.txt", produces = "text/plain")
    public String getLoaderIOToken() {
        return loaderIOToken;
    }
}
