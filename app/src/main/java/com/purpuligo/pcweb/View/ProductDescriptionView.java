package com.purpuligo.pcweb.View;

import com.purpuligo.pcweb.Model.Pojo.ProductSizeDetails;

import java.util.ArrayList;

public interface ProductDescriptionView {

    void showProgressBar();
    void hideProgressBar();
    void showDeliveryTime(String delivery_time, String parent_id);
    void showDeliveryTimeError(String error);
    void showDescription(String description);
    void showDescriptionError(String error);
    void showProductName(String product_name);
    void showProductNameError(String error);
    void showProductPrice(String price);
    void showProductPriceError(String error);
    void showProductStock(String stock);
    void showProductStockError(String error);
    void showImage(String descriptionImage);
    void showImageError(String error);
    void showDeliveryOption(ArrayList<ProductSizeDetails> deliveryOption);
    void showDeliveryOptionError();

    void showSizeList(ArrayList<ProductSizeDetails> productSize);
    void showSizeListError();
    void showSizeRelatedPrice(ArrayList<ProductSizeDetails> productSize, String product_id);
    void showSizeRelatedPriceError();

    //void showItemQuantityInKg(String product_id);
    //void showItemQuantityInKgError();
    void showItemQuantityInNumber();
    void showItemQuantityInNumberError();
    void showWishListedItemSuccess(String message);
    void showWishListedItemFailure();
    void showItemAddedToCartSuccess(String message);
    void showItemAddToCartError();
    void setWishListCount();
    void setWishListCountError();
    void setCartCount();
    void setCartCountError();
    void showOrderQuantity();
    void showOrderQuantityError(String error);
    void parentCategory(String category);
    void navigateToWishList();
    void navigateToCart();
    void showServerError();

//    void setProductListSuccess(ArrayList<ProductListDetails> productListArray);
    void hideProgress();
    void showProgress();


}
