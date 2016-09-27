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


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@ConfigurationProperties(
        prefix = "metrics"
)
@Data
public class MetricsProperties {

    public static final String PREFIX = "metrics.";
    private boolean enabled;
    private Boolean jvm;
    private TimeUnit rateUnit;
    private TimeUnit durationUnit;
    private Boolean showSamples;
    private Set<String> sets;
    private Boolean logback;

    public MetricsProperties() {
        enabled = true;
    }

}
