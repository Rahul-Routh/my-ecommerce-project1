package com.purpuligo.ajeevikafarmfresh.View;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ImageSliderPojo;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductCategoriesDetails;

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
