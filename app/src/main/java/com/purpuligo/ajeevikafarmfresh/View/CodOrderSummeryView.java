package com.purpuligo.ajeevikafarmfresh.View;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartItemList;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartPriceDetails;

import java.util.ArrayList;

public interface CodOrderSummeryView {

    void showProgress();
    void hideProgress();
    void showFirstName(String firstName);
    void showFirstNameError();
    void showLastName(String lastName);
    void showLastNameError();
    void showAddress(String address);
    void showAddressError();
    void showLandmark(String landmark);
    void showLandmarkError();
    void showCity(String city);
    void showCityError();
    void showState(String state);
    void showStateError();
    void showPostalCode(String postalCode);
    void showPostalCodeError();
    void showMobile(String mobileNumber);
    void showMobileError();
    void showProductDetailsList(ArrayList<CartItemList> cartItemArrayList);
    void showProductDetailsListError();
    void showPaymentDetails(ArrayList<CartPriceDetails> cartPriceList, String extra_charge_name, String extra_charge_value);
    void showPaymentDetailsError();
    void showDeliveryTime();
    void showDeliveryTimeError();
    void showSubmitOrderSuccess();
    void showSubmitOrderError();
    void setCartCount();
    void setCartCountError();
    void showOrderPlacedMessage(String message, String order_id);
    void showOrderPlacedFilure(String message);
    void showServerError();
    void showStock(boolean stock);
    void showStockError();
}
