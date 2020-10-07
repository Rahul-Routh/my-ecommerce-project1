package com.purpuligo.ajeevikafarmfresh.View;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ImageSliderPojo;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ParentDetails;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductCategoriesDetails;

import java.util.ArrayList;
import java.util.List;

public interface HomeView {

    void showProgress();
    void hideProgress();
    void showCurrentVersion(String currentVersion);
    void showCurrentVersionError();
    void setRawFishListError();
    void setRawFishListSuccess(ArrayList<ParentDetails> rawFishData);
//    void setRestaurantListError();
//    void setRestaurantListSuccess(ArrayList<ProductCategoriesDetails> restaurantData);
    void setWishListCount(String wishListCount);
    void setWishListCountError();
    void setCartCount(String cartCount);
    void setCartCountError();
    void navigateToWishList();
    void navigateToCart();
    void showAlert(String message);
    void showServerError();
    void showImageSlider(List<ImageSliderPojo> imageSliderList);
    void showImageSliderError();

//    void setRestaurantListSuccess(ArrayList<ProductCategoriesDetails> restaurantData);
}
