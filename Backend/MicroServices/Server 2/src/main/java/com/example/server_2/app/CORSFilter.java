package com.example.server_2.app;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
//import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class CORSFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String origin = requestContext.getHeaderString("Origin");
        responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, Cookie");
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}