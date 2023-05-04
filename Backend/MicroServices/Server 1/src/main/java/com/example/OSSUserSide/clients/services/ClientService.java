package com.example.OSSUserSide.clients.services;

import com.example.OSSUserSide.sessionID.SessionID;
import com.example.OSSUserSide.orders.entities.OrderEntity;
import com.example.OSSUserSide.orders.services.OrderService;
import com.example.OSSUserSide.products.entities.ProductEntity;
import com.example.OSSUserSide.clients.entities.ClientEntity;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Path("/client")
@Stateful
@SessionScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientService implements Serializable {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mysql");

    private final EntityManager em = entityManagerFactory.createEntityManager();

//    @Context
//    private HttpServletRequest request;
    @Context
    HttpServletResponse response;




    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }

    @POST
    @Path("login")
    public Response login(ClientEntity client, @Context HttpServletRequest request) throws IOException {
        ClientEntity tempClient = null;
        SessionID sessionID = null;
        try{
            em.getTransaction().begin();
            String userName = client.getUsername();
            String password = client.getPassword();
            String query = "SELECT a FROM ClientEntity a WHERE a.username = \"" + userName + "\" AND a.password = \"" + password + "\"" ;
            Query q = em.createQuery(query);
            tempClient = (ClientEntity) q.getSingleResult();
        }
        catch(Exception e){
        }
        if (tempClient != null){
            if (request.getSession(false).getAttribute("currentClient") == null) {
                request.getSession().setAttribute("currentClient", tempClient);
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
    public String logout(@Context HttpServletRequest request) {
        request.getSession().invalidate();
        return "User logged out";
    }

    @GET
    @Path("/loggedIn")
    public ClientEntity loggedIn(@Context HttpServletRequest request) {
        if (request.getSession().getAttribute("currentClient") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build().readEntity(ClientEntity.class);
        } else {
            return (ClientEntity) request.getSession().getAttribute("currentClient");
        }
    }
    @POST
    @Path("register")
    public String register(ClientEntity user){
        ClientEntity tempClient = null;
        try{
            em.getTransaction().begin();
            String name = user.getUsername();
            String query = "SELECT a FROM ClientEntity a WHERE a.username = \"" + name + "\"";
            Query q = em.createQuery(query);
            tempClient = (ClientEntity) q.getSingleResult();
        }
        catch(Exception e){
        }
        if (tempClient == null) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return "Client Registered";
        }else {
            return "Client already exists";
        }
    }
    //

    @GET
    @Path("clientsList")
    public List<ClientEntity> getAllClients(){
        em.getTransaction().begin();
        String jpql = "SELECT a FROM ClientEntity a";
        Query query = em.createQuery(jpql);
        List<ClientEntity> admins = query.getResultList();
        em.getTransaction().commit();
        return admins;
    }

    @GET
    @Path("/{id}")
    public ClientEntity getUserById(@PathParam("id") int id) throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        return em.find(ClientEntity.class, id);
    }

    @Path("/{username}")
    public ClientEntity getUserById(@PathParam("username") String username) {
        String jpql = "SELECT a FROM ClientEntity a WHERE a.username = \"" + username + "\"";
        Query query = em.createQuery(jpql);
        return (ClientEntity) query.getSingleResult();
    }

    @POST
    @Path("addToCart/{productId}")
    public String makeOrder(@PathParam("productId") int productId, @Context HttpServletRequest request) throws IOException, InterruptedException {
        ProductEntity productEntity = em.find(ProductEntity.class, productId);
//        ClientEntity clientEntity = em.find(ClientEntity.class, clientId);
        ClientEntity clientEntity = (ClientEntity) request.getSession().getAttribute("currentClient");
        if (clientEntity.getCart().contains(productEntity)) {
            return "Product already in cart";
        }
        clientEntity.getCart().add(productEntity);
        return "Product added to cart";
    }

    //    view cart
    @GET
    @Path("/cart")
    public List<ProductEntity> getCart(@Context HttpServletRequest request) throws IOException, InterruptedException {
        ClientEntity clientEntity = (ClientEntity) request.getSession().getAttribute("currentClient");
//        ClientEntity clientEntity = em.find(ClientEntity.class, id);
        return clientEntity.getCart();
    }

    //    remove from cart
    @DELETE
    @Path("/removeFromCart/{productId}")
    public String removeFromCart(@PathParam("productId") int productId, @Context HttpServletRequest request) throws IOException, InterruptedException {
        ProductEntity productEntity = em.find(ProductEntity.class, productId);
//        ClientEntity clientEntity = em.find(ClientEntity.class, clientId);
        ClientEntity clientEntity = (ClientEntity) request.getSession().getAttribute("currentClient");
        if (clientEntity.getCart().contains(productEntity)) {
            clientEntity.getCart().remove(productEntity);
            return "Product removed from cart";
        }
        return "Product not in cart";
    }

    //    checkout
    @POST
    @Path("/checkout")
    public String checkout(@Context HttpServletRequest request) {
        ClientEntity clientEntity = (ClientEntity) request.getSession().getAttribute("currentClient");
        OrderService orderService = new OrderService();
        for (ProductEntity productEntity : clientEntity.getCart()) {
            orderService.addOrder(clientEntity.getId(), productEntity.getId());
        }
        return "Orders placed";
    }

    //buy product
    @POST
    @Path("/buy/{productId}")
    public String buyProduct(@PathParam("productId") int productId, @Context HttpServletRequest request) {
        ProductEntity productEntity = em.find(ProductEntity.class, productId);
        ClientEntity clientEntity = (ClientEntity) request.getSession().getAttribute("currentClient");
        OrderService orderService = new OrderService();
        orderService.addOrder(clientEntity.getId(), productEntity.getId());
        return "Order placed";
    }




    //    view order history
    @GET
    @Path("/orders")
    public List<OrderEntity> getAllOrders(@Context HttpServletRequest request) throws IOException, InterruptedException, ParseException {
        ClientEntity clientEntity = (ClientEntity) request.getSession().getAttribute("currentClient");
        OrderService orderService = new OrderService();
        return orderService.getClientByID(clientEntity.getId());
    }

    //return messages
    @GET
    @Path("/messages")
    public List<String> getMessages(@Context HttpServletRequest request) {
        ClientEntity clientEntity = (ClientEntity) request.getSession().getAttribute("currentClient");
        String[] messagesArray = clientEntity.getMessages().split(",");
        return Arrays.asList(messagesArray);
    }

    //clear messages
    @POST
    @Path("/clearMessages")
    public String clearMessages(@Context HttpServletRequest request) {
        ClientEntity clientEntity = (ClientEntity) request.getSession().getAttribute("currentClient");
        clientEntity.setMessages("");
        return "Messages cleared";
    }
}
