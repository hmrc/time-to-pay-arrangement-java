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


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.lang.Boolean.TRUE;

@ConfigurationProperties(
        prefix = "auditing"
)
@Data
public class AuditConfigProperties {

    private static final String SINGLE_EVENT_PATH = "write/audit";
    private static final String MERGED_EVENT_PATH = "write/audit/merged";
    private static final String LARGE_MERGED_EVENT_PATH = "write/audit/merged/large";

    public static final String PREFIX = "auditing.";

    private Boolean enabled;
    private List<String> exclusions;
    private Boolean traceRequests = TRUE;
    private String source;
    @Valid
    @NotNull(message = "Missing consumer configuration for auditing")
    private Consumer consumer;


    public String getSingleEventUrl() {
        return createContext()
                .append(SINGLE_EVENT_PATH).toString();
    }

    public String getMergedEventUrl() {
        return createContext()
                .append(MERGED_EVENT_PATH).toString();
    }

    public String getLargeMergedEventUrl() {
        return createContext()
                .append(LARGE_MERGED_EVENT_PATH).toString();
    }

    private StringBuilder createContext() {
        return new StringBuilder()
                .append(consumer.baseUri.getProtocol())
                .append("://")
                .append(consumer.baseUri.getHost())
                .append(":")
                .append(consumer.baseUri.getPort())
                .append("/");
    }

    @Data
    public static class Consumer {

        @Valid
        @NotNull(message = "Missing consumer baseUri for auditing")
        private BaseUri baseUri;

        @Data
        public static class BaseUri {
            @NotNull(message = "Missing consumer host for auditing")
            private String host;
            @NotNull(message = "Missing consumer port for auditing")
            private Integer port;
            private String protocol = "http";
        }
    }
}
