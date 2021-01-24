package com.purpuligo.pcweb.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.pcweb.Constants.Constants;
import com.purpuligo.pcweb.Global.LoginDialog;
import com.purpuligo.pcweb.Global.NetworkState;
import com.purpuligo.pcweb.Model.Adapter.OrderHistoryDetailsAdapter;
import com.purpuligo.pcweb.Model.OrderHistoryDetailsInteractorImpl;
import com.purpuligo.pcweb.Model.Pojo.CartItemList;
import com.purpuligo.pcweb.Presenter.OrderHistoryDetailsPresenter;
import com.purpuligo.pcweb.R;
import com.purpuligo.pcweb.View.OrderHistoryDetailsView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistoryDetailsActivity extends AppCompatActivity implements OrderHistoryDetailsView{

    private String TAG = "OrderHistoryDetailsActivity";
    @BindView(R.id.backBtn) ImageButton backBtn;
    @BindView(R.id.productList) RecyclerView productList;
    @BindView(R.id.shippingFirstName) TextView shippingFirstName;
    @BindView(R.id.shippingLastName) TextView shippingLastName;
    @BindView(R.id.shippingAddress) TextView shippingAddress;
    @BindView(R.id.shippingLandmark) TextView shippingLandmark;
    @BindView(R.id.shippingState) TextView shippingState;
    @BindView(R.id.shippingPostalCode) TextView shippingPostalCode;
    @BindView(R.id.shippingMobileNumber) TextView shippingMobileNumber;

    @BindView(R.id.billingFirstName) TextView billingFirstName;
    @BindView(R.id.billingLastName) TextView billingLastName;
    @BindView(R.id.billingAddress) TextView billingAddress;
    @BindView(R.id.billingLandmark) TextView billingLandmark;
    @BindView(R.id.billingState) TextView billingState;
    @BindView(R.id.billingPostalCode) TextView billingPostalCode;
    @BindView(R.id.billingMobileNumber) TextView billingMobileNumber;
    @BindView(R.id.cartAmount) TextView cartAmount;

    private String order_id;
    private String customer_email;
    private String session_data;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private NetworkState networkState;
    private LoginDialog loginDialog;
    private OrderHistoryDetailsPresenter orderHistoryDetailsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        orderHistoryDetailsPresenter = new OrderHistoryDetailsInteractorImpl(this);
        Intent intent = getIntent();
        try {
            order_id = intent.getStringExtra("order_id");
        }catch (Exception e){e.printStackTrace();}

        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);

        if (networkState.isNetworkAvailable(this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                orderHistoryDetailsPresenter.fetchOrderHistoryAllDetails(order_id);
            }else {loginDialog.loginDialog(this);}
        }else {
            Toast.makeText(this, "Please Check Internet", Toast.LENGTH_SHORT).show();
        }

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
    public void showProductList(ArrayList<CartItemList> orderHistoryProductList) {
        try {
            Log.d(TAG, "showProductList: "+orderHistoryProductList);
            productList.setLayoutManager(new LinearLayoutManager(this));
            productList.setAdapter(new OrderHistoryDetailsAdapter(this,orderHistoryProductList));
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showProductListError() {

    }

    @Override
    public void showShippingFirstName(String firstName) {
        try {
            shippingFirstName.setText(firstName);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showShippingFirstNameError() {

    }

    @Override
    public void showShippingLastName(String lastName) {
        try {
            shippingLastName.setText(lastName);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showShippingLastNameError() {

    }

    @Override
    public void showShippingAddress(String address) {
        try {
            shippingAddress.setText(address);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showShippingAddressError() {

    }

    @Override
    public void showShippingCity(String city) {
        try {
            shippingLandmark.setText(city);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showShippingCityError() {

    }

    @Override
    public void showShippingState(String state) {
        try {
            shippingState.setText(state);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showShippingStateError() {

    }

    @Override
    public void showShippingPostalCode(String postalCode) {
        try {
            shippingPostalCode.setText(postalCode);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showShippingPostalCodeError() {

    }

    @Override
    public void showShippingMobile(String mobileNumber) {
        try {
            shippingMobileNumber.setText(mobileNumber);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showShippingMobileError() {

    }

    @Override
    public void showBillingFirstName(String firstName) {
        try {
            billingFirstName.setText(firstName);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showBillingFirstNameError() {

    }

    @Override
    public void showBillingLastName(String lastName) {
        try {
            billingLastName.setText(lastName);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showBillingLastNameError() {

    }

    @Override
    public void showBillingAddress(String address) {
        try {
            billingAddress.setText(address);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showBillingAddressError() {

    }

    @Override
    public void showBillingCity(String city) {
        try {
            billingLandmark.setText(city);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showBillingCityError() {

    }

    @Override
    public void showBillingState(String state) {
        try {
            billingState.setText(state);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showBillingStateError() {

    }

    @Override
    public void showBillingPostalCode(String postalCode) {
        try {
            billingPostalCode.setText(postalCode);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showBillingPostalCodeError() {

    }

    @Override
    public void showBillingMobile(String mobileNumber) {
        try {
            billingMobileNumber.setText(mobileNumber);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showBillingMobileError() {

    }

    @Override
    public void showTotalAmount(String total_amount) {
        try {
            cartAmount.setText(total_amount);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showTotalAmountError() {

    }

    @Override
    public void showServerError() {
        Snackbar.make(productList,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }
}
