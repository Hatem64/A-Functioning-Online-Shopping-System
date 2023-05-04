package com.example.server_2.app;

import com.example.server_2.admins.services.AdminService;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class app extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(CORSFilter.class);
        classes.add(AdminService.class);
        return classes;
    }

}