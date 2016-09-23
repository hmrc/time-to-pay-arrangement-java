package uk.gov.hmrc.ttpa.metrics;

import ch.qos.logback.classic.Logger;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.json.MetricsModule;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.logback.InstrumentedAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.FALSE;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(value = MetricsProperties.class)
@ConditionalOnProperty(prefix = MetricsProperties.PREFIX, name = "enabled", havingValue = "true")
@Configuration
@EnableMetrics
public class MetricsAutoConfigPlugin extends MetricsConfigurerAdapter {

    private MetricRegistry metricRegistry;
    private ObjectMapper objectMapper;
    private MetricsProperties metricsProperties;

    @Bean
    public FilterRegistrationBean instrumentedFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new InstrumentedFilter());
        filterRegistrationBean.addInitParameter("name-prefix", InstrumentedFilter.REGISTRY_ATTRIBUTE);
        return filterRegistrationBean;
    }


    @Override
    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    @Bean
    public MetricRegistryListener metricRegistryListener() {
        return new MetricRegistryListener();
    }

    public static class MetricRegistryListener implements ServletContextListener {

        @Autowired
        private MetricRegistry metricRegistry;

        @Override
        public void contextInitialized(ServletContextEvent sce) {
            sce.getServletContext().setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, metricRegistry);
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {}
    }


    @PostConstruct
    public void start() {
        log.info("Detected metrics properties configuration...");
        boolean jvmMetricsEnabled = ofNullable(metricsProperties.getJvm()).orElse(FALSE);

        if (jvmMetricsEnabled) {
            ofNullable(metricsProperties.getSets())
                    .orElse(defaultJvmMetrics())
                    .stream()
                    .map(this::createInstance)
                    .filter(x -> x != null)
                    .forEach(m -> metricRegistry.registerAll(m));
        }

        boolean logbackEnabled = ofNullable(metricsProperties.getLogback()).orElse(FALSE);
        if (logbackEnabled) {
            Logger logger = (Logger) log;
            InstrumentedAppender instrumentedAppender = new InstrumentedAppender(metricRegistry);
            instrumentedAppender.setContext(logger.getLoggerContext());
            instrumentedAppender.start();
        }

        TimeUnit rateUnit = ofNullable(metricsProperties.getRateUnit()).orElse(SECONDS);
        TimeUnit durationUnit = ofNullable(metricsProperties.getDurationUnit()).orElse(SECONDS);
        Boolean showSamples = ofNullable(metricsProperties.getShowSamples()).orElse(FALSE);

        MetricsModule metricsModule = new MetricsModule(rateUnit, durationUnit, showSamples);
        objectMapper.registerModule(metricsModule);

        log.info("Metric plugin initialisation complete.");
    }

    @PreDestroy
    public void stop() {
    }

    private MetricSet createInstance(String clazz) {
        try {
            Object e = Class.forName(clazz).newInstance();
            if (e instanceof MetricSet) {
                return (MetricSet) e;
            } else {
                log.warn("Skipping metric " + clazz + " not an instance of MetricSet");
                return null;
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            log.warn("Unable to load metrics class " + clazz, ex);
        }
        return null;
    }


    private Set<String> defaultJvmMetrics() {
        Set<String> jvm = new HashSet<>();
        jvm.add(GarbageCollectorMetricSet.class.getName());
        jvm.add(MemoryUsageGaugeSet.class.getName());
        jvm.add(MemoryUsageGaugeSet.class.getName());
        return jvm;
    }


}
