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


public class Event {

    public enum Type {

        REQUEST_RECEIVED ("RequestReceived"),
        OUTBOUND_CALL ("OutboundCall"),
        TRANSACTION_FAILURE_REASON ("transactionFailureReason"),
        INTERNAL_SERVER_ERROR("ServerInternalError"),
        RESOURCE_NOT_FOUND("ResourceNotFound"),
        SERVER_VALIDATION_ERROR("ServerValidationError");

        private final String description;

        Type(final String description) {
            this.description = description;
        }

    }


    public static class Key {
        public static final String STATUS_CODE = "statusCode";
        public static final String FAILED_REQUEST_REASON = "failedRequestReason";
        public static final String RESPONSE_MESSAGE = "responseMessage";
        public static final String PATH = "path";
        public static final String METHOD = "method";
        public static final String REQUEST_BODY = "requestBody";
        public static final String EXTERNAL_APPLICATION_NAME = "externalApplicationName";
        public static final String TRANSACTION_NAME = "transactionName";
        public static final String USER_AGENT_STRING = "userAgentString";
        public static final String REFERRER = "referrer";
        public static final String INPUT = "input";
        private Key(){}

    }
}
