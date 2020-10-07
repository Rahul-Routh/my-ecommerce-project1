package com.purpuligo.ajeevikafarmfresh.Presenter;

public interface ProductListPresenter {

    void fetchProductListDataFromServer(String category_id);
    void fetchWishListDataFromServer();
    void fetchCartDataFromServer();
}
