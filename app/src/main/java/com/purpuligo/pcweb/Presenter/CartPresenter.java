package com.purpuligo.pcweb.Presenter;

public interface CartPresenter {
    void fetchCartDetailsFromServer(String customer_email, String session_data);
    void removeCartItem(int product_id, int quantity, String email, int cart_id, String session_data);
}
