package com.purpuligo.pcweb.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.purpuligo.pcweb.Global.NetworkState;
import com.purpuligo.pcweb.Global.UserSessionManager;
import com.purpuligo.pcweb.Model.Adapter.StateDetailsListAdapter;
import com.purpuligo.pcweb.Model.Pojo.StateDetails;
import com.purpuligo.pcweb.Model.SignupInteractorImpl;
import com.purpuligo.pcweb.Presenter.SignupPresenter;
import com.purpuligo.pcweb.R;
import com.purpuligo.pcweb.View.SignupView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements SignupView {

    private String TAG = "SignUpActivity";
    //@BindView(R.id.signUpBtn) Button signupBtn;
    @BindView(R.id.backBtn) ImageButton backBtn;
    //@BindView(R.id.newsLetterRadioBtn) RadioGroup newsLetterRadioBtn;
    //@BindView(R.id.privacyPolicyCheckbox) CheckBox privacyPolicyCheckbox;
    @BindView(R.id.signUpToolBar) androidx.appcompat.widget.Toolbar toolbar;
    @BindView(R.id.signUpLayout) LinearLayout signUpLayout;
    //@BindView(R.id.stateSpinner) Spinner stateSpinner;
    private EditText firstName,lastName,email,telephone,fax,company,address1,address2,city,postalCode,country,state,password,repeatPassword;
    private SignupPresenter mSignupPresenter;
    private ProgressDialog progressDialog;
    private NetworkState networkState;
    private StateDetailsListAdapter stateDetailsListAdapter;
    private UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        userSessionManager = new UserSessionManager(this);

        firstName = findViewById(R.id.et_first_name);
        lastName = findViewById(R.id.et_last_name);
        email = findViewById(R.id.et_email);
        telephone = findViewById(R.id.et_telephone);
//        fax = findViewById(R.id.et_fax);
//        company = findViewById(R.id.et_company);
        //address1 = findViewById(R.id.et_address1);
        //address2 = findViewById(R.id.et_address2);
        //city = findViewById(R.id.et_city);
        //postalCode = findViewById(R.id.et_post_code);
        //country = findViewById(R.id.et_country);
        //state = findViewById(R.id.et_state);
        password = findViewById(R.id.et_password);
        repeatPassword = findViewById(R.id.et_repeat_password);

        String jsonData = loadJSONFromAsset();
        Log.d(TAG, "onCreate: "+jsonData);

        mSignupPresenter = new SignupInteractorImpl(this);
        mSignupPresenter.fetchStateDetails(jsonData);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        signUpLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                return true;
//            }
//        });
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void setFirstNameError() {
        firstName.setError("First Name is Empty");
        firstName.requestFocus();
    }

    @Override
    public void setFirstNameValidation() {
        firstName.setError("Please Enter Valid Name");
        firstName.requestFocus();
    }

    @Override
    public void setLastNameError() {
        lastName.setError("Last Name is Empty");
        lastName.requestFocus();
    }

    @Override
    public void setLastNameValidation() {
        lastName.setError("Please Enter Valid Name");
        lastName.requestFocus();
    }

    @Override
    public void setEmailError() {
        email.setError("Email is Empty");
        email.requestFocus();
    }

    @Override
    public void setEmailValidation() {
        email.setError("Please Enter valid Email");
        email.requestFocus();
    }

    @Override
    public void setTelephoneError() {
        telephone.setError("Telephone is Empty");
        telephone.requestFocus();
    }

    @Override
    public void setTelephoneValidation() {
        telephone.setError("Please Enter valid Telephone");
        telephone.requestFocus();
    }

//    @Override
//    public void setFaxError() {
//        fax.setError("Fax Error");
//        fax.requestFocus();
//    }
//
//    @Override
//    public void setCompanyError() {
//        company.setError("Company Error");
//        company.requestFocus();
//    }

    @Override
    public void setAddress1Error() {
        //address1.setError("Address 1 is Empty");
        //address1.requestFocus();
    }

    @Override
    public void setAddress2Error() {
        //address2.setError("Landmark is Empty");
        //address2.requestFocus();
    }

    @Override
    public void setCityError() {
        //city.setError("City Error");
        //city.requestFocus();
    }

    @Override
    public void setPostalCodeError() {
        //postalCode.setError("Postal Code Empty");
        //postalCode.requestFocus();
    }

    @Override
    public void setPostalCodeValidation() {
        //postalCode.setError("Please Enter valid Postal Code");
        //postalCode.requestFocus();
    }

    @Override
    public void setCountryError() {
        //country.setError("Country Error");
        //country.requestFocus();
    }

    @Override
    public void showStateDetailsList(ArrayList<StateDetails> stateDetailsList) {
//        try {
//            Log.d(TAG, "showStateDetailsList: "+stateDetailsList);
//            stateDetailsListAdapter = new StateDetailsListAdapter(this,stateDetailsList);
//            stateSpinner.setAdapter(stateDetailsListAdapter);
//        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void setStateError() {
        //state.setError("State Error");
        //state.requestFocus();
    }

    @Override
    public void setPasswordError() {
        password.setError("Password is Empty");
        password.requestFocus();
    }

    @Override
    public void setRepeatPasswordError() {
        repeatPassword.setError("Password is Empty");
        repeatPassword.requestFocus();
    }

    @Override
    public void setMatchPasswordError() {
        password.setError("Password Not Matched");
        repeatPassword.setError("Password Not Matched");
        password.requestFocus();
    }

    @Override
    public void setRadioButtonError() {
        Toast.makeText(this, "Radio Button Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCheckBoxError() {
        Toast.makeText(this, "Please Accept Terms and Conditions", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToLogin(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showServerError() {
        Snackbar.make(backBtn,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccess(String customer_name) {
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

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplication().getAssets().open("state_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void submitSignUpDetails(View view){
        String country_id = "99";
        if (networkState.isNetworkAvailable(this)){
            //Log.d(TAG, "submitSignUpDetails: "+stateDetailsListAdapter.getZoneId());
            try {
                mSignupPresenter.validateSignupDetails(firstName.getText().toString().trim(),lastName.getText().toString().trim(),
                        email.getText().toString().trim(),telephone.getText().toString().trim(),"City",
                        "Address1","Address2", "1",
                        "0",country_id,password.getText().toString().trim(),
                        repeatPassword.getText().toString());
            }catch (Exception e){e.printStackTrace();}
        }else { Toast.makeText(this, "Check Connection", Toast.LENGTH_SHORT).show(); }
    }
}
