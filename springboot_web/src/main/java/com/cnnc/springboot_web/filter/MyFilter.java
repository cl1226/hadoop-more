package com.cnnc.springboot_web.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(displayName = "Filter", servletNames = "servlet1")
public class MyFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("filter");
        super.doFilter(request, response, chain);
    }

    @Override
    public void init() throws ServletException {
        System.out.println("filter init");
        super.init();
    }

    @Override
    public void destroy() {

    }
}
