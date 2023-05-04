package com.example.OSSUserSide.app;

import com.example.OSSUserSide.admins.services.AdminService;
import com.example.OSSUserSide.clients.services.ClientService;
import com.example.OSSUserSide.orders.services.OrderService;
import com.example.OSSUserSide.products.services.ProductService;
import com.example.OSSUserSide.sellingCompanies.services.SellingCompanyService;
import com.example.OSSUserSide.shippingCompanies.services.ShippingCompanyService;
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
        classes.add(ClientService.class);
        classes.add(OrderService.class);
        classes.add(ProductService.class);
        classes.add(SellingCompanyService.class);
        classes.add(ShippingCompanyService.class);
        return classes;
    }

}