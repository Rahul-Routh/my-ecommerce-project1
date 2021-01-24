package com.purpuligo.pcweb.View;

import com.purpuligo.pcweb.Model.Pojo.ImageSliderPojo;
import com.purpuligo.pcweb.Model.Pojo.ProductCategoriesDetails;

import java.util.ArrayList;
import java.util.List;

public interface CategoryListView {
    void showProgress();
    void hideProgress();
    void setProductListSuccess(ArrayList<ProductCategoriesDetails> categoryListArray);
    void setProductListError();
    void navigateToWishList();
    void navigateToCart();
    void showServerError();
    void showImageSlider(List<ImageSliderPojo> imageSliderList);
    void showImageSliderError();
}
