package com.purpuligo.ajeevikafarmfresh.View;

public interface ForgotPasswordView {

    void showProgressBar();
    void hideProgressBar();
    void setEmailError();
    void setEmailFormatError();
    void setPasswordError();
    void setRepeatPasswordError();
    void setPasswordNotMatched();
    void onSuccess(String message);
    void onFailure(String message);
    void showServerError();
}
