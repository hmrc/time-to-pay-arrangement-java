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

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

import static java.util.Optional.ofNullable;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuditFilter implements Filter {

    private AuditService auditService;
    private AuditConfigProperties auditConfigProperties;
    private AuditEventGenerator auditEventGenerator;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (shouldAudit(httpServletRequest.getRequestURI())) {
            try {
                chain.doFilter(request, response);
            } finally {
                Object event = auditEventGenerator.createEvent(request, response);
                auditService.audit(event);
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
