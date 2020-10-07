package com.purpuligo.ajeevikafarmfresh.View;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductCategoriesDetails;

import java.util.ArrayList;

public interface SubCategoryView {
    void showProgress();
    void hideProgress();
    void setProductListSuccess(ArrayList<ProductCategoriesDetails> categoryListArray);
    void setProductListError();
    void navigateToWishList();
    void navigateToCart();
    void showServerError();
}
