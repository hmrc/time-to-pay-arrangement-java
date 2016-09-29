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


import javaslang.collection.List;
import javaslang.collection.Seq;
import org.springframework.http.HttpHeaders;

public class Request {


    public static class HeaderNames {

        private HeaderNames() {
        }

        public static final String authorisation = HttpHeaders.AUTHORIZATION;
        public static final String xForwardedFor = "x-forwarded-for";
        public static final String xRequestId = "X-Request-ID";
        public static final String xRequestTimestamp = "X-Request-Timestamp";
        public static final String xSessionId = "X-Session-ID";
        public static final String xRequestChain = "X-Request-Chain";
        public static final String trueClientIp = "True-Client-IP";
        public static final String trueClientPort = "True-Client-Port";
        public static final String token = "token";
        public static final String surrogate = "Surrogate";
        public static final String otacAuthorization = "Otac-Authorization";
        public static final String googleAnalyticTokenId = "ga-token";
        public static final String googleAnalyticUserId = "ga-user-cookie-id";
        public static final String deviceID = "deviceID";
        public static final String akamaiReputation = "Akamai-Reputation";


        public static final Seq explicitlyIncludedHeaders = List.of(
                authorisation,
                xForwardedFor,
                xRequestId,
                xRequestTimestamp,
                xSessionId,
                xRequestChain,
                trueClientIp,
                trueClientPort,
                token,
                surrogate,
                otacAuthorization,
                googleAnalyticTokenId,
                googleAnalyticUserId,
                deviceID, // not a typo, should be ID
                akamaiReputation
        );

    }

    public static class CookieNames {
        private CookieNames() {
        }

        public static final String deviceID = "mdtpdi";
    }

    public static class SessionKeys {
        public static final String sessionId = "sessionId";
        public static final String userId = "userId";
        //@deprecated("To be removed. Use internal services lookup instead","2016-06-24")
        public static final String name = "name";
        //@deprecated("To be removed. Use internal services lookup instead","2016-06-24")
        public static final String email = "email";
        //@deprecated("To be removed. Use internal services lookup instead","2016-06-24")
        public static final String agentName = "agentName";
        //@deprecated("Use internal services lookup instead","2016-06-24")
        public static final String token = "token";
        public static final String authToken = "authToken";
        public static final String otacToken = "otacToken";
        //@deprecated("Use internal services lookup instead","2016-06-24")
        public static final String affinityGroup = "affinityGroup";
        //@deprecated("Use internal services lookup instead","2016-06-24")
        public static final String authProvider = "ap";
        public static final String lastRequestTimestamp = "ts";
        public static final String redirect = "login_redirect";
        public static final String npsVersion = "nps-version";
        public static final String sensitiveUserId = "suppressUserIs";
        public static final String postLogoutPage = "postLogoutPage";
        public static final String loginOrigin = "loginOrigin";
        public static final String portalRedirectUrl = "portalRedirectUrl";
        public static final String portalState = "portalState";
    }

}
