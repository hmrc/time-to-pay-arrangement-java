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
package uk.gov.hmrc.ttpa.logging;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.encoder.EncoderBase;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.io.IOUtils.LINE_SEPARATOR;
import static org.apache.commons.io.IOUtils.write;

public class JsonLogstashEncoder extends EncoderBase<ILoggingEvent> {

    private final ObjectMapper mapper = new ObjectMapper().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSZZ";

    @Override
    public void doEncode(ILoggingEvent event) throws IOException {
        final ObjectNode eventNode = mapper.createObjectNode();

        String app = resolveAppName();
        eventNode.put("app", app);
        eventNode.put("hostname", InetAddress.getLocalHost().getHostName());
        eventNode.put("timestamp", convertTime(event.getTimeStamp()));
        eventNode.put("message", event.getFormattedMessage());

        if (event.getThrowableProxy() != null) {
            eventNode.put("exception", ThrowableProxyUtil.asString(event.getThrowableProxy()));
        }
        eventNode.put("logger", event.getLoggerName());
        eventNode.put("thread", event.getThreadName());
        eventNode.put("level", event.getLevel().toString());

        if (getContext() != null) {
            getContext().getCopyOfPropertyMap()
                    .forEach((k, v) -> eventNode.put(k.toLowerCase(), v));
        }

        event.getMDCPropertyMap().forEach((k, v) -> eventNode.put(k.toLowerCase(), v));

        write(mapper.writeValueAsBytes(eventNode), outputStream);
        write(LINE_SEPARATOR, outputStream);
        outputStream.flush();
    }

    private String resolveAppName() {
        return System.getProperty("application.name");
    }

    @Override
    public void close() throws IOException {
        write(LINE_SEPARATOR, outputStream);
    }


    public String convertTime(long time) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date(time));
    }
}
