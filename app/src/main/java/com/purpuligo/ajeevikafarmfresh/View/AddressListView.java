package com.purpuligo.ajeevikafarmfresh.View;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.AddressDetails;

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
