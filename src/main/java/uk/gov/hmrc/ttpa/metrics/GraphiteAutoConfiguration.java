package uk.gov.hmrc.ttpa.metrics;


import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;

@Configuration
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(value = GraphiteProperties.class)
@ConditionalOnProperty(prefix = GraphiteProperties.PREFIX, name = "enabled", havingValue = "true")
public class GraphiteAutoConfiguration {

    private MetricRegistry metricRegistry;
    private GraphiteProperties graphiteProperties;

    @Bean
    public GraphiteReportingManager graphiteReportingManager() {
        return new GraphiteReportingManager(metricRegistry, graphiteProperties);
    }


    @Slf4j
    public static class GraphiteReportingManager {

        private GraphiteReporter graphiteReporter;
        private GraphiteProperties graphiteProperties;

        public GraphiteReportingManager(MetricRegistry metricsRegistry,
                                        GraphiteProperties graphiteProperties) {
            this.graphiteProperties = graphiteProperties;
            this.graphiteReporter = createScheduler(metricsRegistry, graphiteProperties);

        }

        private GraphiteReporter createScheduler(MetricRegistry metricsRegistry,
                                                 GraphiteProperties graphiteProperties) {
            String host = ofNullable(graphiteProperties.getHost()).orElse("localhost");
            Integer port = ofNullable(graphiteProperties.getPort()).orElse(2003);
            String prefix = ofNullable(graphiteProperties.getPrefix())
                    .orElse(String.format("tax.%s", getProperty("application.name")));

            log.info("Configuring GraphiteReporter with host: [{}], port: [{}], prefix: [{}].",
                    host, port, prefix);

            return GraphiteReporter.forRegistry(metricsRegistry)
                    .prefixedWith(prefix)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .filter(MetricFilter.ALL)
                    .build(new Graphite(new InetSocketAddress(host, port)));
        }

        @PostConstruct
        public void init() {
            Long interval = ofNullable(graphiteProperties.getInterval()).orElse(10L);
            graphiteReporter.start(interval, TimeUnit.SECONDS);
            log.info("Graphite started...");
        }

        @PreDestroy
        public void destroy() {
            if (graphiteReporter != null) {
                graphiteReporter.stop();
                log.info("Graphite stopped...");
            }
        }
    }

}
