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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuditEventGenerator {

    private AuditConfigProperties auditConfigProperties;

    private AuditExtensions auditExtensions;

    private HeaderCarrierGenerator headerCarrierGenerator;

    public AuditEvent.DataEvent createSingleAuditEvent(HttpServletRequest request,
                                                       HttpServletResponse httpServletResponse) {


        HeaderCarrier headerCarrier = headerCarrierGenerator.create(request);

        Map<String, String> requiredFields = createRequiredFields(request, httpServletResponse, headerCarrier);
        Map<String, String> tags = createTags(request, headerCarrier);

        AuditEvent.DataEvent dataEvent = new AuditEvent.DataEvent();
        dataEvent.setAuditType(Event.Type.REQUEST_RECEIVED.name());
        dataEvent.setDetail(requiredFields);
        dataEvent.setTags(tags);
        dataEvent.setAuditSource(ofNullable(auditConfigProperties.getSource()).orElse(
                System.getProperty("application.name")
        ));
        dataEvent.setGeneratedAt(now());
        return dataEvent;
    }


    private Map<String, String> createTags(HttpServletRequest request,
                                           HeaderCarrier headerCarrier) {

        Map<String, String> tags = new HashMap<>();
        tags.putAll(auditExtensions.createAuditTags(headerCarrier));
        tags.put(Event.Key.TRANSACTION_NAME, buildQueryUri(request));
        tags.put(Event.Key.PATH, request.getRequestURI());
        return tags;
    }


    private String buildQueryUri(HttpServletRequest req) {
        StringBuffer url = req.getRequestURL();
        String queryString = req.getQueryString();
        if (queryString != null) {
            url.append('?');
            url.append(queryString);
        }
        return url.toString();
    }

    private Map<String, String> createRequiredFields(HttpServletRequest request,
                                                     HttpServletResponse httpServletResponse,
                                                     HeaderCarrier headerCarrier) {

        Map<String, String> requiredFields = new HashMap<>();

        requiredFields.putAll(auditExtensions.createAuditDetails(headerCarrier));
        requiredFields.put(Event.Key.INPUT, format("Request to %s", request.getRequestURI()));
        requiredFields.put(Event.Key.METHOD, request.getMethod().toUpperCase());
        requiredFields.put(Event.Key.USER_AGENT_STRING, ofNullable(request.getHeader(HttpHeaders.USER_AGENT)).orElse("-"));
        requiredFields.put(Event.Key.REFERRER, ofNullable(request.getHeader(HttpHeaders.REFERER)).orElse("-"));
        requiredFields.put(Event.Key.STATUS_CODE, HttpStatus.valueOf(httpServletResponse.getStatus()).toString());
        return requiredFields;
    }

}
