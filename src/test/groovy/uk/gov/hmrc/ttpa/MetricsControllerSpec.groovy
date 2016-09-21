package uk.gov.hmrc.ttpa

class MetricsControllerSpec extends IntegrationSpec {

    def "can get meters count from metrics info"() {
        given:
        (1..10).each {
            testRestTemplate.getForEntity("/terms", String.class)
        }

        when:
        def metrics = testRestTemplate.getForEntity("/admin/metrics", String).getBody()

        then:
        metrics
        def json = mapper.readTree(metrics)
        json.get("meters").get("uk.gov.hmrc.ttpa.metrics.InstrumentedFilter.200").get("count").asInt() != 0

    }

    def "can get counters count from metrics info"() {
        given:
        (1..10).each {
            testRestTemplate.getForEntity("/terms", String.class)
        }

        when:
        def metrics = testRestTemplate.getForEntity("/admin/metrics", String).getBody()

        then:
        metrics
        def json = mapper.readTree(metrics)
        !json.get("counters").get("uk.gov.hmrc.ttpa.metrics.InstrumentedFilter.activeRequests")

    }

    def "can get gauges from metrics info"() {

        when:
        def metrics = testRestTemplate.getForEntity("/admin/metrics", String).getBody()

        then:
        metrics
        def json = mapper.readTree(metrics)
        !json.get("gauges")

    }
}
