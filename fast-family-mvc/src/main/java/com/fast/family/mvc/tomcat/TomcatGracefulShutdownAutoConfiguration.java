package com.fast.family.mvc.tomcat;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "fast.family.tomcat.shutdown.enabled",matchIfMissing = true)
@EnableConfigurationProperties(TomcatGracefulShutdownProperties.class)
@ConditionalOnBean(TomcatEmbeddedServletContainerFactory.class)
@ConditionalOnClass({Servlet.class, Tomcat.class})
public class TomcatGracefulShutdownAutoConfiguration {

    private final TomcatGracefulShutdownProperties tomcatGracefulShutdownProperties;

    public TomcatGracefulShutdownAutoConfiguration(TomcatGracefulShutdownProperties tomcatGracefulShutdownProperties) {
        this.tomcatGracefulShutdownProperties = tomcatGracefulShutdownProperties;
    }

    @Bean
    public TomcatGracefulShutdown tomcatGracefulShutdown(){
        return new TomcatGracefulShutdown(tomcatGracefulShutdownProperties);
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory(){
        TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory = new TomcatEmbeddedServletContainerFactory();
        tomcatEmbeddedServletContainerFactory.addConnectorCustomizers(tomcatGracefulShutdown());
        return tomcatEmbeddedServletContainerFactory;
    }

}
