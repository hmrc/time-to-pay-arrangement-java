package uk.gov.hmrc.ttpa;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/terms")
public class TermsAndConditionsController {

    @GetMapping
    public String terms() {
        log.info("Terms requested..");
        return "Welcome to TTPA";
    }
}
