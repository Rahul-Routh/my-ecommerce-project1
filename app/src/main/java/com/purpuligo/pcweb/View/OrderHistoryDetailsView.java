package com.purpuligo.pcweb.View;

import com.purpuligo.pcweb.Model.Pojo.CartItemList;

import java.util.ArrayList;

public interface OrderHistoryDetailsView {
    void showProgress();
    void hideProgress();
    void showProductList(ArrayList<CartItemList> orderHistoryProductList);
    void showProductListError();
    void showShippingFirstName(String firstName);
    void showShippingFirstNameError();
    void showShippingLastName(String lastName);
    void showShippingLastNameError();
    void showShippingAddress(String address);
    void showShippingAddressError();
    void showShippingCity(String city);
    void showShippingCityError();
    void showShippingState(String state);
    void showShippingStateError();
    void showShippingPostalCode(String postalCode);
    void showShippingPostalCodeError();
    void showShippingMobile(String mobileNumber);
    void showShippingMobileError();
    void showBillingFirstName(String firstName);
    void showBillingFirstNameError();
    void showBillingLastName(String lastName);
    void showBillingLastNameError();
    void showBillingAddress(String address);
    void showBillingAddressError();
    void showBillingCity(String city);
    void showBillingCityError();
    void showBillingState(String state);
    void showBillingStateError();
    void showBillingPostalCode(String postalCode);
    void showBillingPostalCodeError();
    void showBillingMobile(String mobileNumber);
    void showBillingMobileError();
    void showTotalAmount(String total_amount);
    void showTotalAmountError();
    void showServerError();
}
