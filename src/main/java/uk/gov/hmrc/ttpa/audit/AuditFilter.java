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


import javaslang.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static java.util.Optional.ofNullable;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuditFilter extends OncePerRequestFilter {

    private AuditService auditService;
    private AuditConfigProperties auditConfigProperties;
    private AuditEventGenerator auditEventGenerator;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (shouldAudit(request.getRequestURI())) {
            AuditEvent.DataEvent auditEvent = auditEventGenerator.createSingleAuditEvent(request, response);
            try {

                response = new ResponseWrapper(response);
                chain.doFilter(request, response);

                auditService.audit(auditEvent.withDetail(
                        Tuple.of(Event.Key.RESPONSE_MESSAGE, new String(((ResponseWrapper)response).toByteArray()))
                ));
            } catch (Exception e) {
                auditService.audit(auditEvent.withDetail(
                        Tuple.of(Event.Key.FAILED_REQUEST_REASON, e.getMessage())
                ));
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean shouldAudit(String requestUri) {
        return !ofNullable(auditConfigProperties.getExclusions())
                .orElse(new ArrayList<>())
                .contains(requestUri);
    }

    @Override
    public void destroy() {
    }
}
