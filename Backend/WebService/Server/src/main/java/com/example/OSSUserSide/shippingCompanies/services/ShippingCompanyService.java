package com.example.OSSUserSide.shippingCompanies.services;

import com.example.OSSUserSide.orders.entities.OrderEntity;
import com.example.OSSUserSide.orders.services.OrderService;
import com.example.OSSUserSide.shippingCompanies.entities.ShippingCompanyEntity;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import javax.naming.InitialContext;
import java.io.Serializable;
import java.util.List;

@Stateless
//@SessionScoped
@Path("shippingCompany")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShippingCompanyService implements Serializable {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mysql");
    EntityManager em = entityManagerFactory.createEntityManager();
//    @Context
//    private HttpServletRequest request;

    @Resource(mappedName = "java:/jms/queue/OnlineShoppingSystemQ")
    private Queue queue;

    @POST
    @Path("login")
    public String login(ShippingCompanyEntity client) {
        ShippingCompanyEntity tempCompany = null;
        try{
            em.getTransaction().begin();
            String userName = client.getName();
            String query = "SELECT a FROM ShippingCompanyEntity a WHERE a.name = \"" + userName + "\"";
            Query q = em.createQuery(query);
            tempCompany = (ShippingCompanyEntity) q.getSingleResult();
        }
        catch(Exception e){
        }
        if (tempCompany != null){
//            if (request.getSession(false) == null) {
//                request.getSession().setAttribute("currentSCompany", tempCompany);
//            }else {
//                return "Already logged in";
//            }
            return Integer.toString(tempCompany.getId());
        } else
            return "Wrong credentials";
    }

//    @POST
//    @Path("/logout")
//    public String logout() {
//        request.getSession().invalidate();
//        return "User logged out";
//    }
//
//    @GET
//    @Path("/loggedIn")
//    public ShippingCompanyEntity loggedIn() {
//        if (request.getSession().getAttribute("currentSCompany") == null) {
//            return Response.status(Response.Status.UNAUTHORIZED).build().readEntity(ShippingCompanyEntity.class);
//        } else {
//            return (ShippingCompanyEntity) request.getSession().getAttribute("currentSCompany");
//        }
//    }

    @POST
    @Path("addCompany")
    public void addCompany(ShippingCompanyEntity shippingCompany){
        ShippingCompanyEntity tempCompany = null;
        try{
            String query = "SELECT a FROM ShippingCompanyEntity a WHERE a.name = \"" + shippingCompany.getName() + "\" OR a.supportedRegion = \"" + shippingCompany.getSupportedRegion() + "\"" ;
            Query q = em.createQuery(query);
            tempCompany = (ShippingCompanyEntity) q.getSingleResult();
        }
        catch(Exception e){
        }
        if (tempCompany != null) {
            System.out.println(shippingCompany.getName() + " already exists");
        }else {
            em.getTransaction().begin();
            em.persist(shippingCompany);
            em.getTransaction().commit();
        }
    }

    //update sellingCompany
//    @PUT
//    @Path("update")
//    public String updateSellingCompany(SellingCompanyEntity sellingCompanyEntity){
//        SellingCompanyEntity tempClient = null;
//        try{
//            em.getTransaction().begin();
//            String name = sellingCompanyEntity.getName();
//            String query = "SELECT a FROM SellingCompanyEntity a WHERE a.name = \"" + name + "\"";
//            Query q = em.createQuery(query);
//            tempClient = (SellingCompanyEntity) q.getSingleResult();
//        }
//        catch(Exception e){
//        }
//        if (tempClient != null) {
//            em.getTransaction().begin();
//            em.merge(sellingCompanyEntity);
//            em.getTransaction().commit();
//            return "Company updated successfully";
//        }else {
//            return "Company does not exist";
//        }
//    }

    @GET
    @Path("companiesList")
    public List<ShippingCompanyEntity> getAllShippingCompanies(){
        em.getTransaction().begin();
        String jpql = "SELECT a FROM ShippingCompanyEntity a";
        Query query = em.createQuery(jpql);
        List<ShippingCompanyEntity> shippingCompanyEntities = query.getResultList();
        em.getTransaction().commit();
        return shippingCompanyEntities;
    }

    @GET
    @Path("/{id}")
    public ShippingCompanyEntity getCompanyById(@PathParam("id") int id){
        return em.find(ShippingCompanyEntity.class, id);
    }

    @GET
    @Path("/{name}")
    public ShippingCompanyEntity getCompanyByName(@PathParam("name") String name) {
        String jpql = "SELECT a FROM ShippingCompanyEntity a WHERE a.name = \"" + name + "\"";
        Query query = em.createQuery(jpql);
        return (ShippingCompanyEntity) query.getSingleResult();
    }

    @GET
    @Path("/{address}")
    public ShippingCompanyEntity getCompanyByAddress(@PathParam("address") String address) {
        String jpql = "SELECT a FROM ShippingCompanyEntity a WHERE a.supportedRegion = \"" + address + "\"";
        Query query = em.createQuery(jpql);
        return (ShippingCompanyEntity) query.getSingleResult();
    }


    //    view order history
    @GET
    @Path("orders/{id}")
    public List<OrderEntity> getAllOrders(@PathParam("id") int id){
        OrderService orderService = new OrderService();
//        ShippingCompanyEntity shippingCompanyEntity = (ShippingCompanyEntity) request.getSession().getAttribute("currentSCompany");
        ShippingCompanyEntity shippingCompanyEntity = em.find(ShippingCompanyEntity.class, id);
        //update this function to return the non delievered orders only
        return orderService.getShippingCompByID(shippingCompanyEntity.getId());
    }

    //view orders that are not delivered
    @GET
    @Path("orders/notDelivered/{id}")
    public List<OrderEntity> getNotDeliveredOrders(@PathParam("id") int id){
        OrderService orderService = new OrderService();
//        ShippingCompanyEntity shippingCompanyEntity = (ShippingCompanyEntity) request.getSession().getAttribute("currentSCompany");
        ShippingCompanyEntity shippingCompanyEntity = em.find(ShippingCompanyEntity.class, id);
        return orderService.getNotDeliveredOrders(shippingCompanyEntity.getId());
    }

    //    view order by address
    @GET
    @Path("orders/{address}")
    public List<OrderEntity> getOrderByAddress(@PathParam("address") String address){
        OrderService orderService = new OrderService();
        return orderService.getOrdersByAddress(address);
    }

    //change order status to be delivered
//    @PUT
//    @Path("order/{id}/delivered")
//    public void changeOrderStatus(@PathParam("id") int id){
//        OrderService orderService = new OrderService();
//        orderService.updateOrderStatus(id);
//    }

    //check orders that does not have shipping company with the same address
    @GET
    @Path("orders/{address}/notAssigned")
    public List<OrderEntity> getNotAssignedOrders(@PathParam("address") String address){
        OrderService orderService = new OrderService();
        return orderService.getNotAssignedOrders(address);
    }

    //assign order to shipping company

//    @PUT
//    @Path("order/{orderId}/assign")
//    public String assignOrder(@PathParam("orderId") int id){
//        OrderService orderService = new OrderService();
//        ShippingCompanyEntity shippingCompanyEntity = (ShippingCompanyEntity) request.getSession().getAttribute("currentSCompany");
//        return orderService.assignOrder(id, shippingCompanyEntity.getId());
//    }


    //frontend of this one will show a table of the orders, containing the order id, the client id, the product id,
    // and beside them at the right side, a button to change the status of the order to delivered
    //upon clicking the button, the status of the API will contain the product id, the client id, and the request will be standard
    //which is "has been delivered". The API will handle the rest of the process.


    //modify to take order id, and use it to retrienve the clientid and the product id, to update in the message driven service
    @POST
    @Path("submitOrder/{orderId}")
    public void submitOrder(@PathParam("orderId") int orderId){
        try {
            javax.naming.Context context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("java:/ConnectionFactory");
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(this.queue);
            ObjectMessage message = session.createObjectMessage();
            message.setObject(orderId+","+"has been delivered");
            producer.send(message);
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
