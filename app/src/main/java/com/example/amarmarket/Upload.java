package com.example.amarmarket;

public class Upload {
    private String productID,productType,productPrice,productImageUrl,reviews,ordersCount,shopId,category;

    public Upload(){

    }

    public Upload(String productID, String productType, String productPrice,String productImageUrl,String reviews,String ordersCount,String shopId,String category) {
        this.productID = productID;
        this.productType = productType;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.reviews = reviews;
        this.ordersCount = ordersCount;
        this.shopId = shopId;
        this.category = category;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(String ordersCount) {
        this.ordersCount = ordersCount;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
