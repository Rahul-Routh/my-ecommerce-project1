package com.purpuligo.pcweb.View;

import com.purpuligo.pcweb.Model.Pojo.ProductListDetails;

import java.util.ArrayList;

public interface SearchResultView {

    void showProgress();
    void hideProgress();
    void setProductListSuccess(ArrayList<ProductListDetails> productListArray);
    void setProductListError();
    void navigateToWishList();
    void navigateToCart();
    void showServerError();
}
