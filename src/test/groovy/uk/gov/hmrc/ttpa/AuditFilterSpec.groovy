package uk.gov.hmrc.ttpa

import org.mockito.ArgumentCaptor
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestPropertySource
import uk.gov.hmrc.ttpa.audit.AuditEvent
import uk.gov.hmrc.ttpa.audit.AuditService
import uk.gov.hmrc.ttpa.audit.Event

import static org.mockito.Mockito.verify

@TestPropertySource(properties = ["microservice.metrics.graphite.enabled=false", "auditing.enabled=true", "auditing.source=ttpa"])
class AuditFilterSpec extends IntegrationSpec {

    @MockBean
    def AuditService auditService

    def "audit service invoked for implicit request auditing"() {

        when:
        testRestTemplate.getForEntity("/terms", String)

        then:
        def capture = ArgumentCaptor.forClass(AuditEvent.DataEvent)
        verify(auditService).audit(capture.capture())
        def audit = capture.getValue()
        audit.auditSource == "ttpa"
        audit.detail[Event.Key.RESPONSE_MESSAGE] == 'Welcome to TTPA'
    }

}
