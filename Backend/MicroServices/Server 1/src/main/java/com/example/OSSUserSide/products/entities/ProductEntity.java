package com.example.OSSUserSide.products.entities;

import com.example.OSSUserSide.orders.entities.OrderEntity;
import com.example.OSSUserSide.sellingCompanies.entities.SellingCompanyEntity;
import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "products", schema = "onlineshoppingsystemuserside", catalog = "")
public class ProductEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 535)
    private String name;
    @Basic
    @Column(name = "price", nullable = false, precision = 0)
    private double price;
//    @Basic
//    @Column(name = "company_ID", nullable = false)
//    private int companyId;
    @OneToMany(mappedBy = "productId", fetch = FetchType.EAGER)
    @JsonIgnore
    private Collection<OrderEntity> ordersById;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_ID", referencedColumnName = "id", nullable = false)
    private SellingCompanyEntity sellingCompId;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

//    public int retCompanyId() {
//        return companyId;
//    }
//
//    public void setCompanyId(int companyId) {
//        this.companyId = companyId;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductEntity that = (ProductEntity) o;

        if (id != that.id) return false;
        if (Double.compare(that.price, price) != 0) return false;
//        if (companyId != that.companyId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        result = 31 * result + companyId;
        return result;
    }

    public Collection<OrderEntity> getOrdersById() {
        return ordersById;
    }

    public void setOrdersById(Collection<OrderEntity> ordersById) {
        this.ordersById = ordersById;
    }

    public SellingCompanyEntity getSellingCompId() {
        return sellingCompId;
    }

    public void setSellingCompId(SellingCompanyEntity sellingCompId) {
        this.sellingCompId = sellingCompId;
    }
}
