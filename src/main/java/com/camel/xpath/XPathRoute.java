package com.camel.xpath;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class XPathRoute extends RouteBuilder implements Processor {

    @Override
    public void process(Exchange exc) throws Exception {

        String msgId = exc.getIn().getHeader("MSGID", String.class);
    }

    @Override
    public void configure() throws Exception {

        from("{{inputFilePath}}")
                .routeId("xpathRoute")
                .log(LoggingLevel.INFO,"${body}")
                .setHeader("msgid", xpath("item/description/text()"))
                .log(LoggingLevel.INFO, "MsgId>>>>> ${header.msgid}")

                .setHeader("DATA", xpath("RequestParam/DATA/text()"))

                .log(LoggingLevel.INFO, "DATA>>>>> ${header.DATA}")
                .to("{{toRoute}}")
                ;
    }
}
