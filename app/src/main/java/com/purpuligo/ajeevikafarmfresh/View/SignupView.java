package com.purpuligo.ajeevikafarmfresh.View;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.StateDetails;

import java.util.ArrayList;

public interface SignupView {

    void showProgress();
    void hideProgress();
    void setFirstNameError();
    void setFirstNameValidation();
    void setLastNameError();
    void setLastNameValidation();
    void setEmailError();
    void setEmailValidation();
    void setTelephoneError();
    void setTelephoneValidation();
//    void setFaxError();
//    void setCompanyError();
    void setAddress1Error();
    void setAddress2Error();
    void setCityError();
    void setPostalCodeError();
    void setPostalCodeValidation();
    void setCountryError();
    void showStateDetailsList(ArrayList<StateDetails> stateDetailsList);
    void setStateError();
    void setPasswordError();
    void setRepeatPasswordError();
    void setMatchPasswordError();
    void setRadioButtonError();
    void setCheckBoxError();
    void navigateToLogin(String message);
    void showAlert(String message);
    void showServerError();
    void loginSuccess(String customer_name);
    void loginFailure();
}
