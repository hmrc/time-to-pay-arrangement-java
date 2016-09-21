package uk.gov.hmrc.ttpa

class TermsControllerSpec extends IntegrationSpec {

    def "can get terms"() {

        when:
        def terms = testRestTemplate.getForEntity("/terms", String).getBody()

        then:
        terms == 'Welcome to TTPA'

    }
}
