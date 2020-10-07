package com.purpuligo.ajeevikafarmfresh.Presenter;

public interface SignupPresenter {

    void validateSignupDetails(String firstName, String lastName, String email, String telephone, String address1,
                               String address2, String city, String postalCode, String state, String country, String password, String repeatPassword);

    void fetchStateDetails(String stateJson);
}
