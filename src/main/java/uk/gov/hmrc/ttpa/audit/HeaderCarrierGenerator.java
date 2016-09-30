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
import javaslang.Tuple2;
import javaslang.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.gov.hmrc.ttpa.audit.Request.HeaderNames;
import uk.gov.hmrc.ttpa.audit.Request.SessionKeys;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.toHexString;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static uk.gov.hmrc.ttpa.audit.Request.HeaderNames.*;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HeaderCarrierGenerator {

    private Environment environment;

    public HeaderCarrier create(HttpServletRequest request) {
        return ofNullable(request.getSession())
                .map(s -> this.createFromSession(request,s))
                .orElse(createFromHeaders(request));
    }

    private HeaderCarrier createFromHeaders(HttpServletRequest request) {
        return HeaderCarrier.builder()
                .authorisation(request.getHeader(HeaderNames.authorisation))
                .token(request.getHeader(HeaderNames.token))
                .forwarded(forwardedFor(request))
                .sessionId(request.getHeader(xSessionId))
                .requestId(request.getHeader(xRequestId))
                .requestChain(createRequestChain(request))
                .nsStamp(requestTimestamp(request))
                .extraHeaders(new HashSet<>())
                .trueClientIp(request.getHeader(HeaderNames.trueClientIp))
                .trueClientPort(request.getHeader(HeaderNames.trueClientPort))
                .gaToken(request.getHeader(HeaderNames.googleAnalyticTokenId))
                .gaUserId(request.getHeader(HeaderNames.googleAnalyticUserId))
                .deviceId(request.getHeader(HeaderNames.deviceID))
                .akamaiReputation(HeaderNames.akamaiReputation)
                .otherHeaders(otherHeaders(request))
                .build();

    }

    private Collection<Tuple2<String, String>> otherHeaders(HttpServletRequest request) {
        List<String> headerNames = Collections.list(request.getHeaderNames());

        @SuppressWarnings("unchecked")
        List<String> whiteListedHeaders = ofNullable(environment.getProperty("httpHeadersWhitelist", List.class))
                .orElse(new ArrayList<>());

        return headerNames.stream().filter(x ->
                !(HeaderNames.explicitlyIncludedHeaders.contains(x)))
                .filter(whiteListedHeaders::contains)
                .map(h -> Tuple.of(h, request.getHeader(h))).collect(Collectors.toList());
    }

    private Long requestTimestamp(HttpServletRequest request) {

        return ofNullable(request.getHeader(xRequestTimestamp))
                .flatMap(s -> Try.of(() -> Long.valueOf(s)).toJavaOptional())
                .orElse(System.nanoTime());
    }

    private String createRequestChain(HttpServletRequest request) {
        return ofNullable(request.getHeader(HeaderNames.xRequestChain))
                .map(x -> format("%s-%s", x, toHexString(new Random().nextInt() & 0xffff)))
                .orElse(toHexString(new Random().nextInt() & 0xffff));
    }

    private String forwardedFor(HttpServletRequest request) {

        String trueClientIp = ofNullable(request.getHeader(HeaderNames.trueClientIp)).orElse("");
        String xForwardedFor = ofNullable(request.getHeader(HeaderNames.xForwardedFor)).orElse("");
        if (StringUtils.isEmpty(xForwardedFor)) {
            return trueClientIp;
        } else if (StringUtils.isEmpty(trueClientIp)) {
            return xForwardedFor;
        } else if (xForwardedFor.startsWith(trueClientIp)) {
            return xForwardedFor;
        } else {
            return String.format("%s, %s", trueClientIp, xForwardedFor);
        }
    }

    private HeaderCarrier createFromSession(HttpServletRequest request, HttpSession session) {
        List<Cookie> cookies = Arrays.asList(ofNullable(request.getCookies()).orElse(new Cookie[]{}));
        return HeaderCarrier.builder()
                .authorisation((String)session.getAttribute(SessionKeys.authToken))
                .userId((String)session.getAttribute(SessionKeys.userId))
                .token((String)session.getAttribute(SessionKeys.token))
                .forwarded(forwardedFor(request))
                .sessionId(createSessionId(session, request))
                .requestId(request.getHeader(HeaderNames.xRequestId))
                .requestChain(createRequestChain(request))
                .nsStamp(requestTimestamp(request))
                .extraHeaders(new HashSet<>())
                .trueClientIp(request.getHeader(HeaderNames.trueClientIp))
                .trueClientPort(request.getHeader(HeaderNames.trueClientPort))
                .gaToken(request.getHeader(HeaderNames.googleAnalyticTokenId))
                .gaUserId(request.getHeader(HeaderNames.googleAnalyticUserId))
                .deviceId(deviceId(cookies, request))
                .akamaiReputation(HeaderNames.akamaiReputation)
                .otherHeaders(otherHeaders(request)).build();

    }

    private String deviceId(List<Cookie> cookies, HttpServletRequest request) {
        Optional<Cookie> option = cookies.stream()
                .filter(c -> c.getName().equals(Request.CookieNames.deviceID))
                .findFirst();
        return option.map(Cookie::getValue).orElse(request.getHeader(HeaderNames.deviceID));

    }

    private String createSessionId(HttpSession session, HttpServletRequest request) {
        return ofNullable((String)session.getAttribute(SessionKeys.sessionId))
                .orElse(request.getHeader(HeaderNames.xSessionId));

    }

}
