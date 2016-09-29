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
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class AuditEvent {

    private String auditSource;
    private String auditType;
    private String eventId = UUID.randomUUID().toString();
    private Map<String, String> tags;
    private LocalDateTime generatedAt;


    @Data
    public static class DataEvent extends AuditEvent {
        private Map<String, String> detail = new HashMap<>();

        public DataEvent withDetail(Tuple2<String,String> data) {
            detail.put(data._1, data._2);
            return this;
        }
    }

    @Data
    public static class ExtendedDataEvent extends AuditEvent {
        private String detail;
    }

    @Data
    public static class DataCall {

        private Map<String,String> tags;
        private Map<String,String> details;
        private LocalDateTime generatedAt;

    }

    @Data
    public static class MergedDataEvent {
        private String auditSource;
        private String auditType;
        private String eventId = UUID.randomUUID().toString();
        private DataCall request;
        private DataCall response;
    }
}
