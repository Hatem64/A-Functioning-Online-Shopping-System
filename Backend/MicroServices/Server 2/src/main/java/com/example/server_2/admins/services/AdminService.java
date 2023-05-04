package com.example.server_2.admins.services;


import com.example.server_2.admins.entities.AdminsEntity;
import com.example.server_2.admins.entities.SellingCompanyData;
import com.example.server_2.admins.entities.ShippingCompanyData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.EntityTag;


import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;

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
    public void createSellingCompanyAccounts(Map<String, Integer> companyDataList) throws IOException {
        Client client = ClientBuilder.newClient();
        for (Map.Entry<String, Integer> entry : companyDataList.entrySet()) {
            SellingCompanyData data = new SellingCompanyData();
            data.setName(entry.getKey());
            String password = generateRandomPassword(entry.getValue());
            data.setPassword(password);
            Response response = client.target("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/sellingCompany/addCompany")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(data));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                System.out.println("Companies created successfully");
            } else {
                System.out.println("companies not created");
            }
        }
    }
//
    @POST
    @Path("createShippingCompany")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createShippingCompanies(Map<String, String> companies) {
        Client client = ClientBuilder.newClient();
        for (Map.Entry<String, String> entry : companies.entrySet()) {
            ShippingCompanyData data = new ShippingCompanyData();
            data.setName(entry.getKey());
            data.setSupportedRegion(entry.getValue());

            Response response = client.target("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/shippingCompany/addCompany")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(data));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                System.out.println("Companies created successfully");
            } else {
                System.out.println("Companies not created");
            }
        }
    }

//
    @GET
    @Path("listShippingCompanies")
    public HashMap<String, String> listShippingCompanies() throws JsonProcessingException {
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/shippingCompany/companiesList")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            String responseData = response.readEntity(String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            List<HashMap<String, Object>> companiesList = objectMapper.readValue(responseData, List.class);

            HashMap<String, String> result = new HashMap<>();
            for (HashMap<String, Object> company : companiesList) {
                String name = (String) company.get("name");
                String supportedRegion = (String) company.get("supportedRegion");
                result.put(name, supportedRegion);
            }
            return result;
        } else {
            return null;
        }
    }
//
    @GET
    @Path("listSellingCompanies")
    public HashMap<String, String> listSellingCompanies() throws JsonProcessingException {
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/sellingCompany/companiesList")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            String responseData = response.readEntity(String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            List<HashMap<String, Object>> companiesList = objectMapper.readValue(responseData, List.class);

            HashMap<String, String> result = new HashMap<>();
            for (HashMap<String, Object> company : companiesList) {
                String name = (String) company.get("name");
                String supportedRegion = (String) company.get("password");
                result.put(name, supportedRegion);
            }
            return result;
        } else {
            return null;
        }
    }
//
//
//    //list all clients
    @GET
    @Path("listClients")
    public Response listClients() {
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://localhost:8080/OnlineShoppingSystemUserSide-1.0-SNAPSHOT/api/client/clientsList")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            String responseData = response.readEntity(String.class);
            return Response.ok(responseData).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
