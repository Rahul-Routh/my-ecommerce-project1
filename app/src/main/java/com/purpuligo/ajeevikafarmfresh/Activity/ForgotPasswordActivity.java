package com.purpuligo.ajeevikafarmfresh.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.Model.ForgotPasswordInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.Presenter.ForgotPasswordPresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.ForgotPasswordView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity implements ForgotPasswordView{

    private String TAG = "ForgotPasswordActivity";
    @BindView(R.id.enterEmail) EditText enterEmail;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.repeatPassword) EditText repeatPassword;
    @BindView(R.id.submitForgotPassword) Button submitForgotPassword;
    private ForgotPasswordPresenter forgotPasswordPresenter;
    private ProgressDialog progressDialog;
    private NetworkState networkState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        forgotPasswordPresenter = new ForgotPasswordInteractorImpl(this);

        submitForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkState.isNetworkAvailable(ForgotPasswordActivity.this)){
                    forgotPasswordPresenter.validateForgotPassword(enterEmail.getText().toString(),password.getText().toString(),
                            repeatPassword.getText().toString());
                }else { Toast.makeText(ForgotPasswordActivity.this, "Please check internet", Toast.LENGTH_SHORT).show(); }
                Log.d(TAG, "onCreate: "+enterEmail.getText()+"/"+password.getText());
            }
        });
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Processing.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    @Override
    public void setEmailError() {
        enterEmail.setError("Please enter valid email");
    }

    @Override
    public void setEmailFormatError() {
        Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPasswordError() {
        password.setError("Please enter valid password");
    }

    @Override
    public void setRepeatPasswordError() {
        repeatPassword.setError("Empty password");
    }

    @Override
    public void setPasswordNotMatched() {
        password.setError("Password not matched");
        repeatPassword.setError("Password not matched");
    }

    @Override
    public void onSuccess(String message) {
        onBackPressed();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showServerError() {
        Snackbar.make(enterEmail,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }
}
