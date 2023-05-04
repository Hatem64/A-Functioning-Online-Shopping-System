package com.example.OSSUserSide.clients.entities;

import com.example.OSSUserSide.orders.entities.OrderEntity;
import com.example.OSSUserSide.products.entities.ProductEntity;
import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "clients", schema = "onlineshoppingsystemuserside", catalog = "")
public class ClientEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "username", nullable = false, length = 535)
    private String username;
    @Basic
    @Column(name = "password", nullable = false, length = 535)
    private String password;
    @Basic
    @Column(name = "phonenumber", nullable = false)
    private int phonenumber;
    @Basic
    @Column(name = "address", nullable = false, length = 535)
    private String address;
    @Basic
    @Column(name = "messages", nullable = false, length = -1)
    private String messages;
    @OneToMany(mappedBy = "clientId", fetch = FetchType.EAGER)
    @JsonIgnore
    private Collection<OrderEntity> ordersById;
    @Transient
    private List<ProductEntity> cart = new ArrayList<>();

    public List<ProductEntity> getCart() {
        return cart;
    }

    public void setCart(List<ProductEntity> cart) {
        this.cart = cart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(int phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientEntity that = (ClientEntity) o;

        if (id != that.id) return false;
        if (phonenumber != that.phonenumber) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + phonenumber;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    public Collection<OrderEntity> getOrdersById() {
        return ordersById;
    }

    public void setOrdersById(Collection<OrderEntity> ordersById) {
        this.ordersById = ordersById;
    }
}
