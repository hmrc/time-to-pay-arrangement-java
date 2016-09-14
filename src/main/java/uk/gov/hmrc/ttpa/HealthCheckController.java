package uk.gov.hmrc.ttpa;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/ping/ping")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }
}
