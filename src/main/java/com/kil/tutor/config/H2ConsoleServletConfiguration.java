package com.kil.tutor.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class H2ConsoleServletConfiguration {
    @Bean
    ServletRegistrationBean<org.h2.server.web.WebServlet> h2servletRegistration(){
        ServletRegistrationBean<org.h2.server.web.WebServlet> registration = new ServletRegistrationBean<>( new org.h2.server.web.WebServlet());
        registration.addUrlMappings("/h2-console/*");
        registration.addInitParameter("webAllowOthers", "true");
        registration.addInitParameter("webPort", "8081");

        return registration;
    }
}

