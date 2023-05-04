package com.example.OSSUserSide.admins.services;

import com.example.OSSUserSide.admins.entities.AdminsEntity;
import com.example.OSSUserSide.clients.entities.ClientEntity;
import com.example.OSSUserSide.clients.services.ClientService;
import com.example.OSSUserSide.orders.entities.OrderEntity;
import com.example.OSSUserSide.sellingCompanies.entities.SellingCompanyEntity;
import com.example.OSSUserSide.sellingCompanies.services.SellingCompanyService;
import com.example.OSSUserSide.shippingCompanies.entities.ShippingCompanyEntity;
import com.example.OSSUserSide.shippingCompanies.services.ShippingCompanyService;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Stateless
@Path("admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminService {
    private final Map<String, String> sellingCompaniesMap = new HashMap<>();
    private final Map<String, String> shippingCompaniesMap = new HashMap<>();

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private final EntityManager entityManager = emf.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();

    @GET
    @Path("adminsList")
    public List<AdminsEntity> getAllUsers() {
        transaction.begin();
        String jpql = "SELECT a FROM AdminsEntity a";
        Query query = entityManager.createQuery(jpql);
        List<AdminsEntity> admins = query.getResultList();
        transaction.commit();
        return admins;
    }

    @POST
    @Path("login")
    public String login(AdminsEntity admin) {
        try{
            String username=admin.getUsername();
            String password=admin.getPassword();
            Query query=entityManager.createQuery
                    ("SELECT u from AdminsEntity u WHERE u.username = :username and u.password = :password", AdminsEntity.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            AdminsEntity loggedInUser =(AdminsEntity) query.getSingleResult();

            if (loggedInUser != null){
                return "Logged in Successfully";
            } else
                return "Wrong credentials";
        }
        catch(Exception e){
            throw new EJBException(e);
        }
    }

    private String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }


    @POST
    @Path("createProductCompanyAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createSellingCompanyAccounts(Map<String, Integer> accountCreationRequests) {
        SellingCompanyService sellingCompanyService = new SellingCompanyService();
        for (Map.Entry<String, Integer> request : accountCreationRequests.entrySet()) {
            SellingCompanyEntity sellingCompany = new SellingCompanyEntity();
            String companyName = request.getKey();
            int passwordLength = request.getValue();
            String password = generateRandomPassword(passwordLength);
            sellingCompany.setName(companyName);
            sellingCompany.setPassword(password);
            sellingCompanyService.addCompany(sellingCompany);

        }
    }

    @POST
    @Path("createShippingCompany")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createShippingCompanies(Map<String, String> companies) {
        ShippingCompanyService shippingCompanyService = new ShippingCompanyService();
        for (Map.Entry<String, String> company : companies.entrySet()) {
            ShippingCompanyEntity shippingCompany = new ShippingCompanyEntity();
            shippingCompany.setName(company.getKey());
            shippingCompany.setSupportedRegion(company.getValue());
            shippingCompanyService.addCompany(shippingCompany);
        }
    }

    @GET
    @Path("listShippingCompanies")
    public Map<String, String> listShippingCompanies() {
        ShippingCompanyService shippingCompanyService = new ShippingCompanyService();
        List<ShippingCompanyEntity> shippingCompaniesList = shippingCompanyService.getAllShippingCompanies();
        shippingCompaniesMap.clear();
        for (ShippingCompanyEntity shippingCompany : shippingCompaniesList) {
            shippingCompaniesMap.put(shippingCompany.getName(), shippingCompany.getSupportedRegion());
        }
        return shippingCompaniesMap;
    }

    @GET
    @Path("listSellingCompanies")
    public Map<String, String> listSellingCompanies() {
        SellingCompanyService sellingCompanyService = new SellingCompanyService();
        List<SellingCompanyEntity> sellingCompaniesList = sellingCompanyService.getAllSellingCompanies();
        sellingCompaniesMap.clear();
        for (SellingCompanyEntity sellingCompany : sellingCompaniesList) {
            sellingCompaniesMap.put(sellingCompany.getName(), sellingCompany.getPassword());
        }
        return sellingCompaniesMap;
    }


    //list all clients
    @GET
    @Path("listClients")
    public List<ClientEntity> listClients() {
        ClientService clientService = new ClientService();
        List<ClientEntity> clientsList = clientService.getAllClients();
        return clientsList;
    }

    //get addresses that are not assigned to any shipping company
    @GET
    @Path("getUnassignedAddresses")
    public String getUnassignedAddresses() {
        String jpql = "SELECT a FROM OrderEntity a WHERE a.shippingCompId IS NULL";
        Query query = entityManager.createQuery(jpql);
        List<OrderEntity> OrderEntities = query.getResultList();
        String addresses = "";
        for (OrderEntity order : OrderEntities) {
            addresses += order.getAddress() + ", ";
        }
        return addresses;
    }
}
