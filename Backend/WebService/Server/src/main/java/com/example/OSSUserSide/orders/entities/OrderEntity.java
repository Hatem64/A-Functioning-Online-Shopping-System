package com.example.OSSUserSide.orders.entities;

import com.example.OSSUserSide.products.entities.ProductEntity;
import com.example.OSSUserSide.clients.entities.ClientEntity;
import com.example.OSSUserSide.sellingCompanies.entities.SellingCompanyEntity;

import com.example.OSSUserSide.shippingCompanies.entities.ShippingCompanyEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "orders", schema = "onlineshoppingsystemuserside", catalog = "")
public class OrderEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
//    @Column(name = "shippingComp_ID", nullable = false)
//    private int shippingCompId;
    @Basic
    @Column(name = "address", nullable = false, length = 535)
    private String address;
    @Basic
    @Column(name = "delivered", nullable = false)
    private byte delivered;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_ID", referencedColumnName = "id", nullable = false)
    private ClientEntity clientId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_ID", referencedColumnName = "id", nullable = false)
    private ProductEntity productId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sellingComp_ID", referencedColumnName = "id", nullable = false)
    private SellingCompanyEntity sellingCompId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shippingComp_ID", referencedColumnName = "id", nullable = false)
    private ShippingCompanyEntity shippingCompId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public int getClientId() {
//        return clientId;
//    }
//
//    public void setClientId(int clientId) {
//        this.clientId = clientId;
//    }

//    public int getProductId() {
//        return productId;
//    }
//
//    public void setProductId(int productId) {
//        this.productId = productId;
//    }
//
//    public int getSellingCompId() {
//        return sellingCompId;
//    }
//
//    public void setSellingCompId(int sellingCompId) {
//        this.sellingCompId = sellingCompId;
//    }

//    public int getShippingCompId() {
//        return shippingCompId;
//    }
//
//    public void setShippingCompId(int shippingCompId) {
//        this.shippingCompId = shippingCompId;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte getDelivered() {
        return delivered;
    }

    public void setDelivered(byte delivered) {
        this.delivered = delivered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderEntity that = (OrderEntity) o;

        if (id != that.id) return false;
//        if (clientId != that.clientId) return false;
//        if (productId != that.productId) return false;
//        if (sellingCompId != that.sellingCompId) return false;
//        if (shippingCompId != that.shippingCompId) return false;
        if (delivered != that.delivered) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
//        result = 31 * result + clientId;
//        result = 31 * result + productId;
//        result = 31 * result + sellingCompId;
//        result = 31 * result + shippingCompId;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (int) delivered;
        return result;
    }

    public ClientEntity getclientId() {
        return clientId;
    }

    public void setclientId(ClientEntity clientId) {
        this.clientId = clientId;
    }

    public SellingCompanyEntity getsellingCompId() {
        return sellingCompId;
    }

    public void setsellingCompId(SellingCompanyEntity sellingCompId) {
        this.sellingCompId = sellingCompId;
    }

    public ProductEntity getproductId() {
        return productId;
    }

    public void setproductId(ProductEntity productId) {
        this.productId = productId;
    }

    public ShippingCompanyEntity getShippingCompId() {
        return shippingCompId;
    }

    public void setShippingCompId(ShippingCompanyEntity ShippingCompId) {
        this.shippingCompId = ShippingCompId;
    }
}
