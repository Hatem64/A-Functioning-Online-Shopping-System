package com.example.OSSUserSide.sellingCompanies.entities;

import com.example.OSSUserSide.orders.entities.OrderEntity;
import com.example.OSSUserSide.products.entities.ProductEntity;
import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "sellingcompanies", schema = "onlineshoppingsystemuserside", catalog = "")
public class SellingCompanyEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 535)
    private String name;
    @Basic
    @Column(name = "password", nullable = false, length = 535)
    private String password;
    @Basic
    @Column(name = "balance", nullable = false)
    private double balance;
    @OneToMany(mappedBy = "sellingCompId", fetch = FetchType.EAGER)
    @JsonIgnore
    private Collection<OrderEntity> ordersById;
    @OneToMany(mappedBy = "sellingCompId", fetch = FetchType.EAGER)
    @JsonIgnore
    private Collection<ProductEntity> productsById;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addBalance(double balance){this.balance += balance;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SellingCompanyEntity that = (SellingCompanyEntity) o;

        if (id != that.id) return false;
        if (balance != that.balance) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
//        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
//        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = (int) (31 * result + balance);
        return result;
    }

    public Collection<OrderEntity> getOrdersById() {
        return ordersById;
    }

    public void setOrdersById(Collection<OrderEntity> ordersById) {
        this.ordersById = ordersById;
    }

    public Collection<ProductEntity> getProductsById() {
        return productsById;
    }

    public void setProductsById(Collection<ProductEntity> productsById) {
        this.productsById = productsById;
    }
}
