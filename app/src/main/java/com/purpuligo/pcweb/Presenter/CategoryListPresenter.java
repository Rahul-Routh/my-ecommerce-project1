package com.purpuligo.pcweb.Presenter;

public interface CategoryListPresenter {

    void fetchCategoryListFromServer(int category_position);
    void fetchWishListDataFromServer();
    void fetchCartDataFromServer();
    void fetchImageSlider();
}
