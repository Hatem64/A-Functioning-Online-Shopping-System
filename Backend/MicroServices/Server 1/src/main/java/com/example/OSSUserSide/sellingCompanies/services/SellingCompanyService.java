package com.example.OSSUserSide.sellingCompanies.services;

import com.example.OSSUserSide.orders.entities.OrderEntity;
import com.example.OSSUserSide.orders.services.OrderService;
import com.example.OSSUserSide.products.entities.ProductEntity;
import com.example.OSSUserSide.products.services.ProductService;
import com.example.OSSUserSide.sellingCompanies.entities.SellingCompanyEntity;
import com.example.OSSUserSide.sessionID.SessionID;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.util.List;

@Stateful
@SessionScoped
@Path("sellingCompany")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SellingCompanyService implements Serializable {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mysql");
    EntityManager em = entityManagerFactory.createEntityManager();
    @Context
    private HttpServletRequest request;

    @POST
    @Path("login")
    public Response login(SellingCompanyEntity company) {
        SellingCompanyEntity tempCompany = null;
        SessionID sessionID = null;
        try{
            em.getTransaction().begin();
            String userName = company.getName();
            String password = company.getPassword();
            String query = "SELECT a FROM SellingCompanyEntity a WHERE a.name = \"" + userName + "\" AND a.password = \"" + password + "\"" ;
            Query q = em.createQuery(query);
            tempCompany = (SellingCompanyEntity) q.getSingleResult();
        }
        catch(Exception e){
        }
        if (tempCompany != null){
//                            request.getSession().invalidate();
            if (request.getSession(false).getAttribute("currentCompany") == null) {
                request.getSession().setAttribute("currentCompany", tempCompany);
                sessionID = new SessionID("JSESSIONID=" + request.getSession().getId() + ".laptop-501t1dbi; Path=/OnlineShoppingSystemUserSide-1.0-SNAPSHOT;");
            }else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Already logged in").build();
            }
            return Response.status(Response.Status.OK).entity(sessionID).build();
        } else
            return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/logout")
    public String logout() {
        request.getSession().invalidate();
        return "User logged out";
    }

    @GET
    @Path("/loggedIn")
    public SellingCompanyEntity loggedIn() {
        if (request.getSession().getAttribute("currentCompany") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build().readEntity(SellingCompanyEntity.class);
        } else {
            return (SellingCompanyEntity) request.getSession().getAttribute("currentCompany");
        }
    }

    @POST
    @Path("addCompany")
    public void addCompany(SellingCompanyEntity sellingCompany){
        SellingCompanyEntity tempCompany = null;
        try{
            String q = "SELECT a FROM SellingCompanyEntity a WHERE a.name = \"" + sellingCompany.getName() + "\"";
            Query query = em.createQuery(q);
            tempCompany = (SellingCompanyEntity) query.getSingleResult();
        }catch (Exception e) {
        }
        if (tempCompany !=null) {
            System.out.println(sellingCompany.getName() + " already exists");
        }
        else {
            sellingCompany.setBalance(0.0);
            em.getTransaction().begin();
            em.persist(sellingCompany);
            em.getTransaction().commit();
            System.out.println(sellingCompany.getName() + " added");
        }
    }

    //update sellingCompany
    @PUT
    @Path("update")
    public String updateSellingCompany(SellingCompanyEntity sellingCompanyEntity){
        SellingCompanyEntity tempClient = null;
        try{
            em.getTransaction().begin();
            String name = sellingCompanyEntity.getName();
            String query = "SELECT a FROM SellingCompanyEntity a WHERE a.name = \"" + name + "\"";
            Query q = em.createQuery(query);
            tempClient = (SellingCompanyEntity) q.getSingleResult();
        }
        catch(Exception e){
        }
        if (tempClient != null) {
            em.getTransaction().begin();
            em.merge(sellingCompanyEntity);
            em.getTransaction().commit();
            return "Company updated successfully";
        }else {
            return "Company does not exist";
        }
    }

    @GET
    @Path("companiesList")
    public List<SellingCompanyEntity> getAllSellingCompanies(){
        em.getTransaction().begin();
        String jpql = "SELECT a FROM SellingCompanyEntity a";
        Query query = em.createQuery(jpql);
        List<SellingCompanyEntity> sellingCompanyEntities = query.getResultList();
        em.getTransaction().commit();
        return sellingCompanyEntities;
    }

    @GET
    @Path("/{id}")
    public SellingCompanyEntity getUserById(@PathParam("id") int id){
        return em.find(SellingCompanyEntity.class, id);
    }

    @GET
    @Path("/{name}")
    public SellingCompanyEntity getUserByName(@PathParam("name") String name) {
        String jpql = "SELECT a FROM SellingCompanyEntity a WHERE a.name = \"" + name + "\"";
        Query query = em.createQuery(jpql);
        return (SellingCompanyEntity) query.getSingleResult();
    }


    //    view order history
    @GET
    @Path("/orders")
    public List<OrderEntity> getAllOrders(){
        SellingCompanyEntity sellingCompanyEntity = (SellingCompanyEntity) request.getSession().getAttribute("currentCompany");
//        SellingCompanyEntity sellingCompanyEntity = em.find(SellingCompanyEntity.class, id);
        OrderService orderService = new OrderService();
        return orderService.getSellingCompByID(sellingCompanyEntity.getId());
    }

    //add product
    @POST
    @Path("/addProduct/{name}/{price}")
    public String addProduct(@PathParam("name") String name, @PathParam("price") double price){
        SellingCompanyEntity sellingCompanyEntity = (SellingCompanyEntity) request.getSession().getAttribute("currentCompany");
//        SellingCompanyEntity sellingCompanyEntity = em.find(SellingCompanyEntity.class, id);
        ProductService productService = new ProductService();
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(name);
        productEntity.setPrice(price);
        productService.addProduct(productEntity, sellingCompanyEntity.getId());
        return "Product added successfully";
    }

    //get all products of company
    @GET
    @Path("/products")
    public List<ProductEntity> getAllProducts() {
        SellingCompanyEntity sellingCompanyEntity = (SellingCompanyEntity) request.getSession().getAttribute("currentCompany");
        ProductService productService = new ProductService();
        return productService.getCompanyProducts(sellingCompanyEntity.getId());
    }

}
