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
import javaslang.collection.Seq;
import lombok.Builder;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Builder
@Getter
public class HeaderCarrier {

    private String authorisation;
    private String userId;
    private String token;
    private String forwarded;
    private String sessionId;
    private String requestId;
    private Long nsStamp = System.nanoTime();
    private Seq<Tuple2<String, String>> extraHeaders;
    private String trueClientIp;
    private String trueClientPort;
    private String gaToken;
    private String gaUserId;
    private String deviceId;
    private String akamaiReputation;
    private Seq<Tuple2<String, String>> otherHeaders;

    public static HeaderCarrier create(HttpServletRequest request) {

        HeaderCarrier headerCarrier = HeaderCarrier
                .builder()

                .build();

        return headerCarrier;
    }
}
