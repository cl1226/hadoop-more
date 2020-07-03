package com.cnnc.init;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;

public class MyWebApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {

        System.out.println("init....");
    }
}
