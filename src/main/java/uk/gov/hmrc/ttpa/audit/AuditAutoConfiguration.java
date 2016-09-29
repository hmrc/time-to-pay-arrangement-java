/*
 *
 * Copyright 2016 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.ttpa.audit;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(value = {AuditConfigProperties.class})
@Configuration
@EnableAsync
@ConditionalOnProperty(prefix = AuditConfigProperties.PREFIX, name = "enabled", havingValue = "true")
public class AuditAutoConfiguration {

    private AuditConfigProperties auditConfigProperties;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    @Bean
    public AuditService auditService() {
        return new DataStreamAuditService(auditConfigProperties, restTemplate, objectMapper);
    }

    @Bean
    public AuditFilter auditFilter(AuditService auditService,
                                   AuditEventGenerator auditEventGenerator) {
        return new AuditFilter(auditService, auditConfigProperties, auditEventGenerator);
    }

    @Bean
    public AuditEventGenerator auditEventGenerator(AuditExtensions auditExtensions) {
        return new AuditEventGenerator(auditConfigProperties, auditExtensions);
    }

}
