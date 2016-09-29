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

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static uk.gov.hmrc.ttpa.audit.Request.HeaderNames.*;

@Component
public class AuditExtensions {

    public Map<String, String> createAuditTags(HeaderCarrier headerCarrier) {
        Map<String, String> auditTags = new HashMap<>();

        auditTags.put(xRequestId, ofNullable(headerCarrier.getRequestId()).orElse("-"));
        auditTags.put(xSessionId, ofNullable(headerCarrier.getSessionId()).orElse("-"));
        auditTags.put("clientIP", ofNullable(headerCarrier.getTrueClientIp()).orElse("-"));
        auditTags.put("clientPort", ofNullable(headerCarrier.getTrueClientPort()).orElse("-"));
        auditTags.put("Akamai-Reputation", ofNullable(headerCarrier.getTrueClientPort()).orElse("-"));

        return auditTags;
    }


    public Map<String, String> createAuditDetails(HeaderCarrier headerCarrier) {
        Map<String, String> auditDetails = new HashMap<>();

        auditDetails.put("ipAddress", ofNullable(headerCarrier.getForwarded()).orElse("-"));
        auditDetails.put(authorisation, ofNullable(headerCarrier.getAuthorisation()).orElse("-"));
        auditDetails.put(token, ofNullable(headerCarrier.getToken()).orElse("-"));
        auditDetails.put(deviceID, ofNullable(headerCarrier.getDeviceId()).orElse("-"));

        return auditDetails;

    }

}
