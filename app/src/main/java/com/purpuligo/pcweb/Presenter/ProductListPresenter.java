package com.purpuligo.pcweb.Presenter;

public interface ProductListPresenter {

    void fetchProductListDataFromServer(String category_id);
    void fetchWishListDataFromServer();
    void fetchCartDataFromServer();
}
