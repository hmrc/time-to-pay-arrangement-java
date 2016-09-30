package uk.gov.hmrc.ttpa

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@TestPropertySource(properties = ["microservice.metrics.graphite.enabled=false", "auditing.enabled=false"])
class IntegrationSpec extends Specification {

    @Autowired
    def TestRestTemplate testRestTemplate

    @Autowired
    def ObjectMapper mapper

}
