package com.purpuligo.pcweb.Presenter;

public interface AddressListPresenter {
    void fetchAddressListFromServer(String customer_email);
    void removeAddress(String customer_email, int address_id);
}
