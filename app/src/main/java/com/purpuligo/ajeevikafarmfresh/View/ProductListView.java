package com.purpuligo.ajeevikafarmfresh.View;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductListDetails;

import java.util.ArrayList;

public interface ProductListView {

    void showProgress();
    void hideProgress();
    void setProductListSuccess(ArrayList<ProductListDetails> productListArray);
    void setProductListError();
    void navigateToWishList();
    void navigateToCart();
    void showServerError();
}
