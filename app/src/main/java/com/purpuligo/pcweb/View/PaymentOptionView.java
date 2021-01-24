package com.purpuligo.pcweb.View;

import com.purpuligo.pcweb.Model.Pojo.AddressDetails;

import java.util.ArrayList;

public interface PaymentOptionView {
    void showProgressBar();
    void hideProgressBar();
    void showAddressDetails();
    void hideAddressDetails();
    void showAddressId(String addressId);
    void showAddressIdError();
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
    void showAddressList(ArrayList<AddressDetails> addressList);
    void showAddressListError();
    void showServerError();
}
