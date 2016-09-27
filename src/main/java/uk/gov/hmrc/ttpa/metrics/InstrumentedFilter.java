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
package uk.gov.hmrc.ttpa.metrics;

import com.codahale.metrics.servlet.AbstractInstrumentedFilter;

import java.util.HashMap;
import java.util.Map;

public class InstrumentedFilter extends AbstractInstrumentedFilter {
    public static final String REGISTRY_ATTRIBUTE = InstrumentedFilter.class.getName();

    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int NO_CONTENT = 204;
    private static final int BAD_REQUEST = 400;
    private static final int NOT_FOUND = 404;
    private static final int SERVER_ERROR = 500;

    /**
     * Creates a new instance of the filter.
     */
    public InstrumentedFilter() {
        super(REGISTRY_ATTRIBUTE, createMeterNamesByStatusCode(), "other");
    }

    private static Map<Integer, String> createMeterNamesByStatusCode() {
        final Map<Integer, String> meterNamesByStatusCode = new HashMap<Integer, String>(6);
        meterNamesByStatusCode.put(OK, "" + OK);
        meterNamesByStatusCode.put(CREATED, "" + CREATED);
        meterNamesByStatusCode.put(NO_CONTENT, "" + NO_CONTENT);
        meterNamesByStatusCode.put(BAD_REQUEST, "" + BAD_REQUEST);
        meterNamesByStatusCode.put(NOT_FOUND, "" + NOT_FOUND);
        meterNamesByStatusCode.put(SERVER_ERROR, "" + SERVER_ERROR);
        return meterNamesByStatusCode;
    }
}
