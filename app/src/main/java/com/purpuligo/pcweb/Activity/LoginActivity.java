package com.purpuligo.pcweb.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.pcweb.Global.NetworkState;
import com.purpuligo.pcweb.Model.LoginInteractorImpl;
import com.purpuligo.pcweb.Presenter.LoginPresenter;
import com.purpuligo.pcweb.R;
import com.purpuligo.pcweb.View.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.loginBtn) Button btnLogin;
    @BindView(R.id.registerCreateAccount) TextView registerCreateAccount;
    @BindView(R.id.forgotPassword) TextView forgotPassword;
    @BindView(R.id.skipForNow) TextView skipForNow;
    @BindView(R.id.pb_load) ProgressBar progressBar;
    @BindView(R.id.loginUsername) EditText userName;
    @BindView(R.id.loginPassword) EditText password;
    @BindView(R.id.loginLayout) LinearLayout loginLayout;
    private LoginPresenter mLoginPresenter;
    private NetworkState networkState;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        networkState = new NetworkState();

        mLoginPresenter = new LoginInteractorImpl(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkState.isNetworkAvailable(LoginActivity.this)){
//                    validUser();
                    mLoginPresenter.validateLoginDetails(userName.getText().toString().trim(),password.getText().toString().trim());
                    Log.d("tag","tag : "+userName.getText().toString()+"="+password.getText().toString());
                }else {
                    Toast.makeText(LoginActivity.this, "Connect to the Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkState.isNetworkAvailable(LoginActivity.this)){
                    navigateToSignUp();
                }else { Toast.makeText(LoginActivity.this, "Connect to the Internet", Toast.LENGTH_SHORT).show(); }

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkState.isNetworkAvailable(LoginActivity.this)){
                    navigateToForgotPassword();
                }else {
                    Toast.makeText(LoginActivity.this, "Connect to the Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        loginLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                try {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                }catch (Exception e){e.printStackTrace();}
//                return true;
//            }
//        });

        skipForNow.setPaintFlags(skipForNow.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        skipForNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkState.isNetworkAvailable(LoginActivity.this)){
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    intent.putExtra("customer_name","Guest");
                    intent.putExtra("show_delivery_dialog","1");
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Connect to the Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void showProgress() {
        //progressDialog = new ProgressDialog(this);
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        //progressDialog.dismiss();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUsernameError() {
        progressBar.setVisibility(View.GONE);
        userName.setError("Username Error");
        userName.requestFocus();
    }

    @Override
    public void setPasswordError() {
        progressBar.setVisibility(View.GONE);
        password.setError("Password Error");
        password.requestFocus();
    }

    @Override
    public void loginSuccess(String customer_name) {
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this,HomeActivity.class);
        intent.putExtra("customer_name",customer_name);
        intent.putExtra("show_delivery_dialog","1");
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailure() {
        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToSignUp() {
        startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
    }

    @Override
    public void navigateToForgotPassword() {
        startActivity(new Intent(this,ForgotPasswordActivity.class));
    }

    @Override
    public void showAlert(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showServerError() {
        Snackbar.make(btnLogin,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }

//    private boolean validUser() {
//        String emailInput = userName.getText().toString().trim();
//        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
//            userName.setError("Not a valid email address. Should be your@email.com");
//            return false;
//        } else {
//            userName.setError(null);
//            return true;
//        }
//    }

}
