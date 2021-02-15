package com.camel.xpath;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.language.xpath.XPathBuilder;
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
                .setHeader("testPaylod", xpath("envelop/dBody/testPaylod/text()"))
                .log(LoggingLevel.INFO, "MsgId>>>>> ${header.testPaylod}")

                .process(new Processor(){

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String test = exchange.getIn().getHeader("testPaylod",String.class).trim();
                        String data =null;
                        String header=null;

                        if(test.contains("MsgTypeRequest")){
                             data = XPathBuilder.xpath("//*[local-name()='DATA']/text()")
                                    .evaluate(exchange.getContext()  ,test,String.class);
                             header = XPathBuilder.xpath("//*[local-name()='HEADER']/text()")
                                    .evaluate(exchange.getContext()  ,test,String.class);
                        }
                        System.out.println(">>>> data "+ data +  " : "+header);
                        exchange.getIn().setHeader("RESULT",data + header);
                    }
                })

                .log(LoggingLevel.INFO, "${header.RESULT}")
                .to("{{toRoute}}")
                ;
    }
}
