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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Async
public class DataStreamAuditService implements AuditService {

    private AuditConfigProperties auditConfigProperties;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;


    @Override
    public void audit(AuditEvent.DataEvent event) {
        log.info("Sending audit event '{}' to datastream", event);
        ResponseEntity<String> response;
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(event), httpHeaders);
            response = restTemplate.exchange(auditConfigProperties.getSingleEventUrl(), HttpMethod.POST, entity, String.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.info("Response from datastream '{}'", response.getStatusCode());
    }

}
