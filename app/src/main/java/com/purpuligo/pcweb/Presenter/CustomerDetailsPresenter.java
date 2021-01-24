package com.purpuligo.pcweb.Presenter;

public interface CustomerDetailsPresenter {

    void validateSubmittedCustomerDetails(String customer_email, String firstName, String lastName, String telephone, String address1,
                                          String address2, String city, String state, int country, String postalCode);

    void fetchStateDetails(String stateJson);
}
