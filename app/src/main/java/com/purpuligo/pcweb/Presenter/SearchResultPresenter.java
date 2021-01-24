package com.purpuligo.pcweb.Presenter;

public interface SearchResultPresenter {
    void fetchProductListDataFromServer(String search_query);
    void fetchWishListDataFromServer();
    void fetchCartDataFromServer();
}
