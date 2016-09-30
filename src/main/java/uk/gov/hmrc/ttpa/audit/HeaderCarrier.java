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


import javaslang.Tuple2;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.Set;

@Builder
@Getter
public class HeaderCarrier {

    private String authorisation;
    private String userId;
    private String token;
    private String forwarded;
    private String sessionId;
    private String requestId;
    private String requestChain;
    private Long nsStamp = System.nanoTime();
    @Singular
    private Set<Tuple2<String, String>> extraHeaders;
    private String trueClientIp;
    private String trueClientPort;
    private String gaToken;
    private String gaUserId;
    private String deviceId;
    private String akamaiReputation;
    @Singular
    private Set<Tuple2<String, String>> otherHeaders;

}
