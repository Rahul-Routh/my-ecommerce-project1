package com.purpuligo.ajeevikafarmfresh.View;

public interface LoginView {

    void showProgress();
    void hideProgress();
    void setUsernameError();
    void setPasswordError();
    void loginSuccess(String customer_name);
    void loginFailure();
    void navigateToSignUp();
    void navigateToForgotPassword();
    void showAlert(String message);
    void showServerError();
}
