package com.example.a2.ThanhToan;

public class CartDetail {
    private String cartID;
    private String productID;
    private int amount;
    private int Price;

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    private String key;

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public CartDetail() {
    }

    public CartDetail(String cartID, String productID, int amount, int Price) {
        this.cartID = cartID;
        this.productID = productID;
        this.amount = amount;
        this.Price = Price;
    }
}
