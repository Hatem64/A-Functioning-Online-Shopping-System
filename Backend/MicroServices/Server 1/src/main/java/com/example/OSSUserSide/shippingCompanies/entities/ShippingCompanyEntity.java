package com.example.OSSUserSide.shippingCompanies.entities;

import com.example.OSSUserSide.orders.entities.OrderEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "shippingcompanies", schema = "onlineshoppingsystemuserside", catalog = "")
public class ShippingCompanyEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 535)
    private String name;
//    @Basic
//    @Column(name = "password", nullable = false, length = 535)
//    private String password;
    @Basic
    @Column(name = "supportedRegion", nullable = false, length = 535)
    private String supportedRegion;

    @OneToMany(mappedBy = "shippingCompId")
    @JsonIgnore
    private Collection<OrderEntity> ordersById;

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

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getSupportedRegion() {
        return supportedRegion;
    }

    public void setSupportedRegion(String supportedRegion) {
        this.supportedRegion = supportedRegion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShippingCompanyEntity that = (ShippingCompanyEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
//        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (supportedRegion != null ? !supportedRegion.equals(that.supportedRegion) : that.supportedRegion != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
//        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (supportedRegion != null ? supportedRegion.hashCode() : 0);
        return result;
    }

    public Collection<OrderEntity> getOrdersById() {
        return ordersById;
    }

    public void setOrdersById(Collection<OrderEntity> ordersById) {
        this.ordersById = ordersById;
    }
}
