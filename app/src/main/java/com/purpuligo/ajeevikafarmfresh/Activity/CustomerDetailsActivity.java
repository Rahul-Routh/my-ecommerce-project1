package com.purpuligo.ajeevikafarmfresh.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.LoginDialog;
import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.StateDetailsListAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.CustomerDetailsInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.StateDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.CustomerDetailsPresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.CustomerDetailsView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerDetailsActivity extends AppCompatActivity implements CustomerDetailsView{

    private String TAG = "CustomerDetailsActivity";
    @BindView(R.id.et_first_name) EditText firstName;
    @BindView(R.id.et_last_name) EditText lastName;
    @BindView(R.id.et_telephone) EditText telephone;
    @BindView(R.id.et_address1) EditText address1;
    @BindView(R.id.et_address2) EditText address2;
    @BindView(R.id.et_city) EditText city;
    @BindView(R.id.et_country) EditText country;
    @BindView(R.id.stateSpinner) Spinner stateSpinner;
    @BindView(R.id.et_post_code) EditText postalCode;
    @BindView(R.id.backBtn) ImageButton backBtn;

    private String customer_email;
    private String session_data;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private NetworkState networkState;
    private LoginDialog loginDialog;
    private CustomerDetailsPresenter customerDetailsPresenter;
    private StateDetailsListAdapter stateDetailsListAdapter;
    private String previousActivityValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        customerDetailsPresenter = new CustomerDetailsInteractorImpl(this);

        Intent intent = getIntent();
        previousActivityValue = intent.getStringExtra("previousActivityValue");

        String jsonData = loadJSONFromAsset();
        Log.d(TAG, "onCreate: "+jsonData);
        customerDetailsPresenter.fetchStateDetails(jsonData);

        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
        session_data = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_SESSION_DATA,"");
        Log.d(TAG, "onCreate: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,""));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Processing.....");
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
    public void setTelephoneError() {
        telephone.setError("Telephone is Empty");
        telephone.requestFocus();
    }

    @Override
    public void setTelephoneValidation() {
        telephone.setError("Please Enter valid Telephone");
        telephone.requestFocus();
    }

    @Override
    public void setAddress1Error() {
        address1.setError("Address 1 is Empty");
        address1.requestFocus();
    }

    @Override
    public void setAddress2Error() {
        address2.setError("Address 2 is Empty");
        address2.requestFocus();
    }

    @Override
    public void setCityError() {
        city.setError("City is Empty");
        city.requestFocus();
    }

    @Override
    public void setPostalCodeError() {
        postalCode.setError("Postal Code Empty");
        postalCode.requestFocus();
    }

    @Override
    public void setPostalCodeValidation() {
        postalCode.setError("Please Enter valid Postal Code");
        postalCode.requestFocus();
    }

    @Override
    public void setStateDetailsList(ArrayList<StateDetails> stateDetailsList) {
        try {
            Log.d(TAG, "setStateDetailsList: "+stateDetailsList);
            stateDetailsListAdapter = new StateDetailsListAdapter(this,stateDetailsList);
            stateSpinner.setAdapter(stateDetailsListAdapter);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (previousActivityValue.equalsIgnoreCase("CheckoutAddressListActivity")){
            startActivity(new Intent(CustomerDetailsActivity.this,CheckoutAddressListActivity.class));
            finish();
        }else {
            startActivity(new Intent(CustomerDetailsActivity.this,AddressListActivity.class));
            finish();
        }
        //onBackPressed();
        //startActivity(new Intent(this,HomeActivity.class));

    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (previousActivityValue.equalsIgnoreCase("CheckoutAddressListActivity")){
            startActivity(new Intent(CustomerDetailsActivity.this,CheckoutAddressListActivity.class));
            finish();
        }else {
            startActivity(new Intent(CustomerDetailsActivity.this,AddressListActivity.class));
            finish();
        }
        //onBackPressed();
        //startActivity(new Intent(this,HomeActivity.class));
        //finish();
    }

    @Override
    public void showServerError() {
        Snackbar.make(backBtn,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
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

    public void updateUserDetails(View view){
        int state_id = 99;
        if (networkState.isNetworkAvailable(CustomerDetailsActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                try {
                    if (!stateDetailsListAdapter.getZoneId().equalsIgnoreCase("0000") & stateDetailsListAdapter.getZoneId() != null){
                        Log.d(TAG, "submitSignUpDetails: "+stateDetailsListAdapter.getZoneId());
                    customerDetailsPresenter.validateSubmittedCustomerDetails(customer_email,firstName.getText().toString(),
                            lastName.getText().toString(),telephone.getText().toString(),address1.getText().toString(),
                            address2.getText().toString(),city.getText().toString().trim(),stateDetailsListAdapter.getZoneId(),state_id,
                            postalCode.getText().toString());
                    }else { Toast.makeText(this, "Please Select State", Toast.LENGTH_SHORT).show(); }
                }catch (Exception e){e.printStackTrace();}
            }else {loginDialog.loginDialog(CustomerDetailsActivity.this);}
        }else {
            Toast.makeText(CustomerDetailsActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show(); }
    }
}
