package uk.gov.hmrc.ttpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
class TermsControllerSpec extends Specification {

    @Autowired
    def TestRestTemplate  testRestTemplate


    def "can get terms"() {

        when:
        def terms = testRestTemplate.getForEntity("/terms", String).getBody()

        then:
        terms == 'Welcome to TTPA'

    }
}
