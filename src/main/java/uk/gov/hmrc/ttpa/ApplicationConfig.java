package uk.gov.hmrc.ttpa;

import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmrc.ttpa.metrics.InstrumentedFilter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Configuration
@EnableConfigurationProperties
public class ApplicationConfig {

    @Bean
    public FilterRegistrationBean instrumentedFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new InstrumentedFilter());
        filterRegistrationBean.addInitParameter("name-prefix", InstrumentedFilter.REGISTRY_ATTRIBUTE);
        return filterRegistrationBean;
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
        public void contextDestroyed(ServletContextEvent sce) {

        }
    }
}
