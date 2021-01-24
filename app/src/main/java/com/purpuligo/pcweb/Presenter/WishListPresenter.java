package com.purpuligo.pcweb.Presenter;

public interface WishListPresenter {
    void fetchWishListDataFromServer(String customer_email);
    void removeSelectedWishListItem(String customer_email, String product_id);
}
