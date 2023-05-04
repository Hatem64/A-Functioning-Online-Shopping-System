package com.example.OSSUserSide.shippingCompanies.MessageDriven;

import com.example.OSSUserSide.orders.entities.OrderEntity;
import com.example.OSSUserSide.shippingCompanies.services.ShippingCompanyService;
import com.example.OSSUserSide.clients.entities.ClientEntity;
import com.example.OSSUserSide.clients.services.ClientService;
import com.example.OSSUserSide.orders.services.OrderService;
//import com.mysql.cj.protocol.Message;
//import com.mysql.cj.protocol.MessageListener;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

@MessageDriven(
        activationConfig = {
                @jakarta.ejb.ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
                @jakarta.ejb.ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/OnlineShoppingSystemQ")
        },
        mappedName = "java:/jms/queue/OnlineShoppingSystemQ", name = "OrderMessageBean")
public class ShippingCompanyMessageDriven implements MessageListener {
    ShippingCompanyService shippingCompanyService = new ShippingCompanyService();
    OrderService orderService = new OrderService();
    ClientService clientService = new ClientService();


    @Override
    public void onMessage(Message message) {
        try {
            String orderRequest = message.getBody(String.class);
            String[] orderRequestArray = orderRequest.split(",");
            OrderService orderService = new OrderService();
            int orderId = Integer.parseInt(orderRequestArray[0]);
            OrderEntity orderEntity = orderService.getOrderByID(orderId);
            ClientEntity clientEntity = orderEntity.getclientId();
            orderService.updateOrderStatus(orderEntity.getId());
            if (clientEntity.getMessages().equals(""))
                clientEntity.setMessages(orderEntity.getproductId().getName()+ " " + orderRequestArray[1]);
            else{
                clientEntity.setMessages(clientEntity.getMessages() + ","+ orderEntity.getproductId().getName()+ " " + orderRequestArray[1]);
            }
            // process the order request here

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
