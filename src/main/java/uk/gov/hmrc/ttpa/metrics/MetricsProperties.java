package uk.gov.hmrc.ttpa.metrics;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@ConfigurationProperties(
        prefix = "metrics"
)
@Data
public class MetricsProperties {

    public  static final String PREFIX = "metrics.";
    private boolean enabled;
    private Boolean jvm;
    private Graphite graphite;
    private TimeUnit rateUnit;
    private TimeUnit durationUnit;
    private Boolean showSamples;
    private Set<String> sets;
    private Boolean logback;

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
