package com.purpuligo.ajeevikafarmfresh.Presenter;

public interface HomePresenter {

    void fetchCurrentVersion();
    void fetchDataFromServer();
    void fetchWishListDataFromServer();
    void fetchCartDataFromServer();
    void fetchWishListCount(String customer_email);
    void fetchCartCount(String customer_email, String session_data);
    void fetchImageSlider();

}
