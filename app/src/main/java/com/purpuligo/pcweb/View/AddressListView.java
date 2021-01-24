package com.purpuligo.pcweb.View;

import com.purpuligo.pcweb.Model.Pojo.AddressDetails;

import java.util.ArrayList;

public interface AddressListView {
    void showProgressBar();
    void hideProgressBar();
    void showAddressList(ArrayList<AddressDetails> addressList);
    void showAddressListError();
    void showServerError();
    void removeAddressSuccess();
    void removeAddressFailure();
}
