package uk.gov.hmrc.ttpa.metrics;


import ch.qos.logback.classic.Logger;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.json.MetricsModule;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.codahale.metrics.logback.InstrumentedAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.FALSE;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(path="/admin/metrics", produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MetricsController {

    private MetricsProperties metricsProperties;

    private MetricRegistry metricRegistry;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        metricRegistry.registerAll(new GarbageCollectorMetricSet());
        metricRegistry.registerAll(new MemoryUsageGaugeSet());
        metricRegistry.registerAll(new ThreadStatesGaugeSet());

        Logger logger = (Logger) log;

        InstrumentedAppender instrumentedAppender = new InstrumentedAppender(metricRegistry);
        instrumentedAppender.setContext(logger.getLoggerContext());
        instrumentedAppender.start();


        TimeUnit rateUnit =
                ofNullable(metricsProperties.getRateUnit())
                .orElse(SECONDS);

        TimeUnit durationUnit = ofNullable(metricsProperties.getDurationUnit())
                .orElse(SECONDS);

        Boolean showSamples = ofNullable(metricsProperties.getShowSamples())
                .orElse(FALSE);

        MetricsModule metricsModule = new MetricsModule(rateUnit, durationUnit, showSamples);
        objectMapper.registerModule(metricsModule);

    }

    @GetMapping
    public ResponseEntity<String> metric() throws Exception {
        String metricsAsJson = objectMapper.writeValueAsString(metricRegistry);
        return ResponseEntity.ok()
                .header("Cache-Control", "must-revalidate,no-cache,no-store")
                .body(metricsAsJson);
    }
}
