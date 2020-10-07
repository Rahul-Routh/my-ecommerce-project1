package com.purpuligo.ajeevikafarmfresh.View;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.StateDetails;

import java.util.ArrayList;

public interface EditUserDetailsView {

    void showProgress();
    void hideProgress();
    void setFirstNameError();
    void setFirstNameValidation();
    void setLastNameError();
    void setLastNameValidation();
    void setTelephoneError();
    void setTelephoneValidation();
    void setAddress1Error();
    void setAddress2Error();
    void setCityError();
    void setPostalCodeError();
    void setPostalCodeValidation();
    void setStateDetailsList(ArrayList<StateDetails> stateDetailsList);
    void onSuccess(String message);
    void onFailure(String message);
    void showServerError();
}
