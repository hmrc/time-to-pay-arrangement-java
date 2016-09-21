package uk.gov.hmrc.ttpa

import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = "metrics.enabled=false")
class DisabledMetricsControllerSpec extends IntegrationSpec {


    def "no metrics info returned if plugin not configured"() {
        given:
        (1..10).each {
            testRestTemplate.getForEntity("/terms", String.class)
        }

        when:
        def metrics = testRestTemplate.getForEntity("/admin/metrics", String).getBody()

        then:
        def json = mapper.readTree(metrics)
        json.get("metrics").isEmpty(null)
        json.get("names").isEmpty(null)
        json.get("gauges").isEmpty(null)
        json.get("counters").isEmpty(null)
        json.get("histograms").isEmpty(null)
        json.get("meters").isEmpty(null)
        json.get("timers").isEmpty(null)

    }
}
