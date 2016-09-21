package uk.gov.hmrc.ttpa.metrics;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "microservice.metrics.graphite"
)
@Data
public class GraphiteProperties {

    public static final String PREFIX = "microservice.metrics.graphite.";
    private String host;
    private Integer port;
    private String prefix;
    private boolean enabled;
    private Long interval;
}

