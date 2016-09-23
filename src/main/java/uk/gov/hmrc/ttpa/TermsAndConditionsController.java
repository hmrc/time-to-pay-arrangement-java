package uk.gov.hmrc.ttpa;


import com.codahale.metrics.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/terms")
public class TermsAndConditionsController {

    @GetMapping
    @Timed(absolute = true, name = "api.terms")
    public String terms() {
        log.info("Terms requested.. '{}'", LocalDateTime.now());
        return "Welcome to TTPA";
    }
}
