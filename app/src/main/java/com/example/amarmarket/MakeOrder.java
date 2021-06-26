package com.example.amarmarket;

public class MakeOrder {
    String shopId,customerId,orderId,date,productId,contact,address;

    public MakeOrder(){

    }

    public MakeOrder(String customerId, String orderId, String date,String productId,String contact, String address) {
        this.customerId = customerId;
        this.orderId = orderId;
        this.date = date;
        this.productId = productId;
        this.contact = contact;
        this.address = address;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
