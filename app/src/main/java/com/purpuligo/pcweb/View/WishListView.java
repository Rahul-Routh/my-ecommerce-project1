package com.purpuligo.pcweb.View;


import com.purpuligo.pcweb.Model.Pojo.WishListItems;

import java.util.ArrayList;

public interface WishListView {

    void showProgressBar();
    void hideProgressBar();
    void showWishList(ArrayList<WishListItems> wishListArray);
    void showWishListError();
    void showRemovedProductFromWishList(String message);
    void showRemovedProductFromWishListError();
    void showServerError();
}
