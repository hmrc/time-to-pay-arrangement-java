package uk.gov.hmrc.ttpa.metrics;


import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(path = "/admin/metrics", produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MetricsController {


    private MetricRegistry metricRegistry;

    private ObjectMapper objectMapper;


    @GetMapping
    public ResponseEntity<String> metric() throws Exception {
        String metricsAsJson = objectMapper.writeValueAsString(metricRegistry);
        return ResponseEntity.ok()
                .header("Cache-Control", "must-revalidate,no-cache,no-store")
                .body(metricsAsJson);
    }
}
