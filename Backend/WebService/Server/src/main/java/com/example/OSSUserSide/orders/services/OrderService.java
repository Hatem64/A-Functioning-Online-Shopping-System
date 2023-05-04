package com.example.OSSUserSide.orders.services;

import com.example.OSSUserSide.orders.entities.OrderEntity;
import com.example.OSSUserSide.products.entities.ProductEntity;
import com.example.OSSUserSide.shippingCompanies.services.ShippingCompanyService;
import com.example.OSSUserSide.clients.entities.ClientEntity;
import com.example.OSSUserSide.sellingCompanies.entities.SellingCompanyEntity;
import com.example.OSSUserSide.shippingCompanies.entities.ShippingCompanyEntity;
import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.io.Serializable;
import java.util.List;

@Path("/orders")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderService implements Serializable {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mysql");

    private final EntityManager em = entityManagerFactory.createEntityManager();

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }


//    @POST
//    @Path("addOrder")
//    public void addOrder(OrderEntity order){
//        em.getTransaction().begin();
//        em.persist(order);
//        em.getTransaction().commit();
//    }

    @POST
    @Path("addOrder/{clientId}/{productId}")
    public void addOrder(@PathParam("clientId") int clientId, @PathParam("productId") int productId){
        OrderEntity order = new OrderEntity();
        ClientEntity client = em.find(ClientEntity.class, clientId);
        ProductEntity product = em.find(ProductEntity.class, productId);
        SellingCompanyEntity sellingCompany = em.find(SellingCompanyEntity.class, product.getSellingCompId().getId());
        order.setclientId(client);
        order.setproductId(product);
        order.setsellingCompId(sellingCompany);
        sellingCompany.addBalance(product.getPrice());
        order.setAddress(client.getAddress());
        ShippingCompanyService shippingCompanyService = new ShippingCompanyService();
        ShippingCompanyEntity shippingCompany = shippingCompanyService.getCompanyByAddress(client.getAddress());
        order.setShippingCompId(shippingCompany);
        order.setDelivered((byte) 0);
        em.getTransaction().begin();
        em.persist(order);
        em.getTransaction().commit();
    }

    //update order shipping company
    @PUT
    @Path("updateShippingComp/{id}/{shippingCompId}")
    public void updateShippingComp(@PathParam("id") int id, @PathParam("shippingCompId") int shippingCompId){
        OrderEntity order = em.find(OrderEntity.class, id);
        ShippingCompanyEntity shippingCompany = em.find(ShippingCompanyEntity.class, shippingCompId);
        order.setShippingCompId(shippingCompany);
        em.getTransaction().begin();
        em.persist(order);
        em.getTransaction().commit();
    }

    //get all orders with null shipping company
    @GET
    @Path("nullShippingCompList")
    public List<OrderEntity> getNullShippingComp(){
        String jpql = "SELECT a FROM OrderEntity a WHERE a.shippingCompId IS NULL";
        Query query = em.createQuery(jpql);
        List<OrderEntity> orderEntities = query.getResultList();
        return orderEntities;
    }

    @GET
    @Path("ordersList")
    public List<OrderEntity> getAllOrders(){
        String jpql = "SELECT a FROM  OrderEntity a";
        Query query = em.createQuery(jpql);
        List<OrderEntity> orderEntities = query.getResultList();
        return orderEntities;
    }

    @GET
    @Path("orderByID/{id}")
    public OrderEntity getOrderByID(@PathParam("id") int id){
        String jpql = "SELECT a FROM OrderEntity a WHERE a.id = " + id;
        Query query = em.createQuery(jpql);
        OrderEntity orderEntities = (OrderEntity) query.getSingleResult();
        return orderEntities;
    }

    @GET
    @Path("clientByID/{id}")
    public List<OrderEntity> getClientByID(@PathParam("id") int id){
        String jpql = "SELECT a FROM OrderEntity a WHERE a.clientId = " + id;
        Query query = em.createQuery(jpql);
        List<OrderEntity> orderEntities = query.getResultList();
        return orderEntities;
    }

    @GET
    @Path("productByID/{id}")
    public List<OrderEntity> getProductByID(@PathParam("id") int id){
        String jpql = "SELECT a FROM OrderEntity a WHERE a.productId = " + id;
        Query query = em.createQuery(jpql);
        List<OrderEntity> orderEntities = query.getResultList();
        return orderEntities;
    }

    @GET
    @Path("sellingCompByID/{id}")
    public List<OrderEntity> getSellingCompByID(@PathParam("id") int id){
        String jpql = "SELECT a FROM OrderEntity a WHERE a.sellingCompId = " + id;
        Query query = em.createQuery(jpql);
        List<OrderEntity> orderEntities = query.getResultList();
        return orderEntities;
    }

    @GET
    @Path("shippingCompByID/{id}")
    public List<OrderEntity> getShippingCompByID(@PathParam("id") int id){
        String jpql = "SELECT a FROM OrderEntity a WHERE a.shippingCompId = " + id;
        Query query = em.createQuery(jpql);
        List<OrderEntity> orderEntities = query.getResultList();
        return orderEntities;
    }

    //get orders by address
    @GET
    @Path("address/{address}")
    public List<OrderEntity> getOrdersByAddress(@PathParam("address") String address){
        String jpql = "SELECT a FROM OrderEntity a WHERE a.address = '" + address + "'";
        Query query = em.createQuery(jpql);
        List<OrderEntity> orderEntities = query.getResultList();
        return orderEntities;
    }

//    @GET
//    @Path("deliveredByID/{id}")
//    public List<OrderEntity> getDeliveredByID(@PathParam("id") int id){
//        String jpql = "SELECT a FROM OrderEntity a WHERE a.delivered = " + id;
//        Query query = em.createQuery(jpql);
//        List<OrderEntity> orderEntities = query.getResultList();
//        return orderEntities;
//    }

    @PUT
    @Path("updateOrderStatus/{id}/delivered")
    public void updateOrderStatus(@PathParam("id") int id){
        OrderEntity order = em.find(OrderEntity.class, id);
        order.setDelivered((byte) 1);
        em.getTransaction().begin();
        em.persist(order);
        em.getTransaction().commit();
//        return "Order is delivered successfully";
    }



    @GET
    @Path("orders/{address}/notAssigned")
    public List<OrderEntity> getNotAssignedOrders(@PathParam("address") String address){
        String jpql = "SELECT a FROM OrderEntity a WHERE a.address = '" + address + "' AND a.shippingCompId IS NULL";
        Query query = em.createQuery(jpql);
        List<OrderEntity> orderEntities = query.getResultList();
        return orderEntities;
    }

    //Use this ya ziad
    //get orders by client id, product id, and not delivered
    @GET
    @Path("orders/{companyId}/notDelivered")
    public List<OrderEntity> getNotDeliveredOrders(@PathParam("companyId") int companyId){
        String jpql = "SELECT a FROM OrderEntity a\n" +
                "JOIN FETCH a.clientId\n" +
                "JOIN FETCH a.productId WHERE a.shippingCompId = " + companyId + " AND a.delivered = 0";
        Query query = em.createQuery(jpql);
        List<OrderEntity> orderEntities = query.getResultList();
        return orderEntities;
    }

    //assign order to shipping company
    @PUT
    @Path("assignOrder/{id}/{shippingCompId}")
    public String assignOrder(@PathParam("id") int id, @PathParam("shippingCompId") int shippingCompId){
        OrderEntity order = em.find(OrderEntity.class, id);
        ShippingCompanyEntity shippingCompany = em.find(ShippingCompanyEntity.class, shippingCompId);
        order.setShippingCompId(shippingCompany);
        em.getTransaction().begin();
        em.persist(order);
        em.getTransaction().commit();
        return "Order is assigned successfully";
    }
}