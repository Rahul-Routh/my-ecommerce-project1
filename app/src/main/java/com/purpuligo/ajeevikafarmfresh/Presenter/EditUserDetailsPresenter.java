package com.purpuligo.ajeevikafarmfresh.Presenter;

public interface EditUserDetailsPresenter {
    void validateSubmittedCustomerDetails(String customer_email, String addressId, String firstName, String lastName, String telephone,
                                          String address1, String address2, String city, String postalCode, String state, int country);

    void fetchStateDetails(String stateJson);
}
