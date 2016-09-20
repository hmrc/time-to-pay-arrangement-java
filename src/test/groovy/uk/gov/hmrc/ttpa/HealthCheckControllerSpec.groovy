package uk.gov.hmrc.ttpa

import org.springframework.http.HttpStatus
import spock.lang.Specification

class HealthCheckControllerSpec extends IntegrationSpec {

    def "can ping ping"() {

        when:
        def response = testRestTemplate.getForEntity("/ping/ping", String)

        then:
        response.statusCode == HttpStatus.OK
    }
}
