package com.example.OSSUserSide.products.services;

import com.example.OSSUserSide.products.entities.ProductEntity;
import com.example.OSSUserSide.sellingCompanies.entities.SellingCompanyEntity;
import jakarta.ejb.EJBException;
import jakarta.ejb.Singleton;
import jakarta.persistence.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Singleton
@Path("products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductService {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mysql");
    EntityManager em = entityManagerFactory.createEntityManager();

    @GET
    @Path("productsList")
    public List<ProductEntity> getProduct() {
        em.getTransaction().begin();
        String jpql = "SELECT a FROM ProductEntity a";
        Query query = em.createQuery(jpql);
        List<ProductEntity> productsEntities = query.getResultList();
        em.getTransaction().commit();
        return productsEntities;
    }

    @GET
    @Path("viewProduct/{Id}")
    public ProductEntity getProductById(@PathParam("Id") int Id)
    {
        ProductEntity productEntity = em.find(ProductEntity.class, Id);
        return productEntity;
    }

    @GET
    @Path("viewProductsOfCompany/{Id}")
    public List<ProductEntity> getCompanyProducts(@PathParam("Id") int Id)
    {
        String query = "SELECT a FROM ProductEntity a WHERE a.sellingCompId = " + Id;
        Query q = em.createQuery(query);
        List<ProductEntity> productsEntities = q.getResultList();
        return productsEntities;
    }

    @POST
    @Path("addProduct/{Id}")
    public String addProduct(ProductEntity productEntity, @PathParam("Id") int num)
    {
        Query query = em.createQuery("SELECT a FROM SellingCompanyEntity a WHERE a.id = " + num);
        List<SellingCompanyEntity> sellingcompaniesEntities = query.getResultList();
        productEntity.setSellingCompId(sellingcompaniesEntities.get(0));
        try
        {
            em.persist(productEntity);
            return "Success";
        }
        catch (Exception e)
        {
            throw new EJBException(e);
        }
    }



}
