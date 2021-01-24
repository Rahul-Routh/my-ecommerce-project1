package com.purpuligo.pcweb.Model.Pojo;

public class CartItemList {

    private String product_id;
    private String title;
    private String is_gift_product;
    private String id_product_attribute;
    private int id_address_delivery;
    private boolean stock;
    private String discount_price;
    private int discount_percentage;
    private String images;
    private String price;
    private String quantity;
    private String model;
    private String product_items;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIs_gift_product() {
        return is_gift_product;
    }

    public void setIs_gift_product(String is_gift_product) {
        this.is_gift_product = is_gift_product;
    }

    public String getId_product_attribute() {
        return id_product_attribute;
    }

    public void setId_product_attribute(String id_product_attribute) {
        this.id_product_attribute = id_product_attribute;
    }

    public int getId_address_delivery() {
        return id_address_delivery;
    }

    public void setId_address_delivery(int id_address_delivery) {
        this.id_address_delivery = id_address_delivery;
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public int getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(int discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProduct_items() {
        return product_items;
    }

    public void setProduct_items(String product_items) {
        this.product_items = product_items;
    }
}
