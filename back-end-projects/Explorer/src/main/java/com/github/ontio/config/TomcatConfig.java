package com.github.ontio.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @Autowired
    private ParamsConfig paramsConfig;

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory tomcatFactory = new TomcatServletWebServerFactory();
        tomcatFactory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
//        tomcatFactory.setPort(8081);
        return tomcatFactory;
    }
    class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {

        @Override
        public void customize(Connector connector) {
            Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
            //设置最大连接数 默认值 10000
//            protocol.setMaxConnections(10000);
            //设置最大线程数
            protocol.setMaxThreads(paramsConfig.TOMCAT_MAXTHREAD);
//            protocol.setConnectionTimeout(20000);
        }
    }
}