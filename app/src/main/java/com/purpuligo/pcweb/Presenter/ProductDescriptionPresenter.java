package com.purpuligo.pcweb.Presenter;

import java.util.ArrayList;

public interface ProductDescriptionPresenter {

    void fetchDescriptionDataFromServer(String product_id);
    void saveWishListItemOnServer(String customer_email, String product_id);
    void addRawFishItemToCart(String customer_email, String session_data, int quantity_in_kg, int product_id, String product_option_id,
                              ArrayList<String> product_option_value_id,
                              String size_product_option_id, String size_product_option_value_id);
    void addRestaurantItemToCart(String customer_email, String session_data, int quantity, int product_id);
    void gotoWishList();
    void gotoCart();
}
