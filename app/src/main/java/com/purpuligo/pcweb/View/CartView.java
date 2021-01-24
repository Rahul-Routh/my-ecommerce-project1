package com.purpuligo.pcweb.View;

import com.purpuligo.pcweb.Model.Pojo.CartItemList;
import com.purpuligo.pcweb.Model.Pojo.CartPriceDetails;

import java.util.ArrayList;

public interface CartView {

    void showProgressBar();
    void hideProgressBar();
    void showCartItemList(ArrayList<CartItemList> cartItemArrayList);
    void showCartItemListError(String count);
    void showCartPriceDetailsList(ArrayList<CartPriceDetails> cartPriceList);
    void showCartPriceDetailsListError();
    void removeItemSuccess(String message);
    void showServerError();
}
