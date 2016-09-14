package uk.gov.hmrc.ttpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
class HealthCheckControllerSpec extends Specification{

    @Autowired
    def TestRestTemplate testRestTemplate

    def "can ping ping"() {

        when:
        def response = testRestTemplate.getForEntity("/ping/ping", String)

        then:
        response.statusCode == HttpStatus.OK
    }
}
