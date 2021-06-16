package com.example.amarmarket;

public class Upload {
    private String productID,productType,productPrice,productImageUrl;

    public Upload(){

    }

    public Upload(String productID, String productType, String productPrice,String productImageUrl) {
        this.productID = productID;
        this.productType = productType;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
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
}
