package uk.gov.hmrc.ttpa.metrics;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ConfigurationProperties(
        prefix = "metrics"
)
@Data
@Component
public class MetricsProperties {

    private boolean enabled;
    private List<String> jvm = new ArrayList<>();
    private Graphite graphite;
    private TimeUnit rateUnit;
    private TimeUnit durationUnit;
    private Boolean showSamples;

    public MetricsProperties() {
        enabled = true;
    }

    @Data
    public static class Graphite {
        private String host;
        private Integer port;
        private String prefix;
    }
}
