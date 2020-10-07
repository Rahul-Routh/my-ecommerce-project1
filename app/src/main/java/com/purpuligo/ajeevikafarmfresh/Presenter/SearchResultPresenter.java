package com.purpuligo.ajeevikafarmfresh.Presenter;

public interface SearchResultPresenter {
    void fetchProductListDataFromServer(String search_query);
    void fetchWishListDataFromServer();
    void fetchCartDataFromServer();
}
