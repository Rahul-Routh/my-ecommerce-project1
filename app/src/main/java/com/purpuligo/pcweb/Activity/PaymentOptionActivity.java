package com.purpuligo.pcweb.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.pcweb.Constants.Constants;
import com.purpuligo.pcweb.Global.LoginDialog;
import com.purpuligo.pcweb.Global.NetworkState;
import com.purpuligo.pcweb.Model.Adapter.CheckoutAddressListAdapter;
import com.purpuligo.pcweb.Model.PaymentOptionInteractorImpl;
import com.purpuligo.pcweb.Model.Pojo.AddressDetails;
import com.purpuligo.pcweb.Presenter.PaymentOptionPresenter;
import com.purpuligo.pcweb.R;
import com.purpuligo.pcweb.View.PaymentOptionView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentOptionActivity extends AppCompatActivity implements PaymentOptionView{

    private String TAG = "PaymentOptionActivity";
    @BindView(R.id.backBtn) ImageButton backBtn;
    @BindView(R.id.changeAddress) TextView changeAddress;
    @BindView(R.id.customerFirstName) TextView customerFirstName;
    @BindView(R.id.customerLastName) TextView customerLastName;
    @BindView(R.id.customerAddress) TextView customerAddress;
    //@BindView(R.id.customerLandmark) TextView customerLandmark;
    @BindView(R.id.customerState) TextView customerState;
    @BindView(R.id.customerPostalCode) TextView customerPostalCode;
    @BindView(R.id.customerMobileNumber) TextView customerMobileNumber;
    @BindView(R.id.customerCity) TextView customerCity;
    //@BindView(R.id.totalPrice) TextView totalPriceTextView;
    //@BindView(R.id.checkoutRecyclerView) RecyclerView checkoutRecyclerView;
    @BindView(R.id.addressDetailsLayout) LinearLayout addressDetailsLayout;

    private String addressId;
    private String customer_email;
    private String session_data;
    private String totalPrice;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private NetworkState networkState;
    private LoginDialog loginDialog;
    private PaymentOptionPresenter paymentOptionPresenter;
    private CheckoutAddressListAdapter checkoutAddressListAdapter;
    private ArrayList<String> deliveryPinCodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        paymentOptionPresenter = new PaymentOptionInteractorImpl(this);

        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
        session_data = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_SESSION_DATA,"");
        Log.d(TAG, "onCreate: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,""));

        Intent intent = getIntent();
        addressId = intent.getStringExtra("addressId");
        customerFirstName.setText(intent.getStringExtra("firstName"));
        customerLastName.setText(intent.getStringExtra("lastName"));
        customerAddress.setText(intent.getStringExtra("address")+" , "+intent.getStringExtra("landmark"));
        //customerLandmark.setText(intent.getStringExtra("landmark"));
        customerState.setText(intent.getStringExtra("state"));
        customerPostalCode.setText(intent.getStringExtra("postalCode"));
        customerMobileNumber.setText(intent.getStringExtra("mobile"));
        customerCity.setText(intent.getStringExtra("city"));
        totalPrice = intent.getStringExtra("totalPrice");
        //totalPriceTextView.setText(totalPrice);

        deliveryPinCodeList = new ArrayList<>();
           deliveryPinCodeList.add("721507");
//        deliveryPinCodeList.add("834001");
//        deliveryPinCodeList.add("834002");
//        deliveryPinCodeList.add("834003");
//        deliveryPinCodeList.add("834004");
//        deliveryPinCodeList.add("834005");
//        deliveryPinCodeList.add("834006");
//        deliveryPinCodeList.add("834007");
//        deliveryPinCodeList.add("834008");
//        deliveryPinCodeList.add("834009");

        if (intent.getStringExtra("address") == null){
            if (networkState.isNetworkAvailable(this)){
                if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                    paymentOptionPresenter.fetchAddressListFromServer(customer_email);
                }else {loginDialog.loginDialog(this);}
            }else {Toast.makeText(PaymentOptionActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
        }

        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkState.isNetworkAvailable(PaymentOptionActivity.this)){
                    if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                        startActivity(new Intent(PaymentOptionActivity.this,CheckoutAddressListActivity.class)
                                .putExtra("totalPrice",totalPrice));
                        finish();
                    }else {loginDialog.loginDialog(PaymentOptionActivity.this);}
                }else {Toast.makeText(PaymentOptionActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
    public void showAddressDetails() {
        addressDetailsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddressDetails() {
        addressDetailsLayout.setVisibility(View.GONE);
    }

    @Override
    public void showAddressId(String address_Id) {
        try {
            Log.d(TAG, "showAddressId: "+address_Id);
            addressId = address_Id;
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showAddressIdError() {

    }

    @Override
    public void showFirstName(String firstName) {
        try {
            Log.d(TAG, "showFirstName: "+firstName);
            customerFirstName.setText(firstName);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showFirstNameError() {

    }

    @Override
    public void showLastName(String lastName) {
        try {
            Log.d(TAG, "showLastName: "+lastName);
            customerLastName.setText(lastName);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showLastNameError() {

    }

    @Override
    public void showAddress(String address) {
        try {
            Log.d(TAG, "showAddress: "+address);
            customerAddress.setText(address);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showAddressError() {

    }

    @Override
    public void showLandmark(String landmark) {
        try {
            Log.d(TAG, "showLandmark: "+landmark);
            //customerLandmark.setText(landmark);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showLandmarkError() {

    }

    @Override
    public void showCity(String city) {
        try {
            Log.d(TAG, "showCity: "+city);
            customerCity.setText(city);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showCityError() {

    }

    @Override
    public void showState(String state) {
        try {
            Log.d(TAG, "showState: "+state);
            customerState.setText(state);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showStateError() {

    }

    @Override
    public void showPostalCode(String postalCode) {
        try {
            Log.d(TAG, "showPostalCode: "+postalCode);
            customerPostalCode.setText(postalCode);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showPostalCodeError() {

    }

    @Override
    public void showMobile(String mobileNumber) {
        try {
            Log.d(TAG, "showMobile: "+mobileNumber);
            customerMobileNumber.setText(mobileNumber);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showMobileError() {

    }

    @Override
    public void showAddressList(ArrayList<AddressDetails> addressList) {
        try {
            Log.d(TAG, "showAddressList: "+addressList);
            //checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            //checkoutAddressListAdapter = new CheckoutAddressListAdapter(this,addressList);
            //checkoutRecyclerView.setAdapter(checkoutAddressListAdapter);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showAddressListError() {

    }

    @Override
    public void showServerError() {
        Snackbar.make(backBtn,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }

    public void payCod(View view){
        try{
            //String addressId = checkoutAddressListAdapter.getAddressId();
            if (networkState.isNetworkAvailable(PaymentOptionActivity.this)){
                if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                    if (addressId != null){
                        if (deliveryPinCodeList.contains(customerPostalCode.getText().toString().trim())){
                            Log.d(TAG, "contains pin code: ");
                            Log.d(TAG, "payCod: "+addressId);
                            Intent codOrderSummery = new Intent(PaymentOptionActivity.this,CodOrderSummeryActivity.class);
                            codOrderSummery.putExtra("payment_method","codPayment");
                            codOrderSummery.putExtra("placeOrderPaymentType","Place COD Order");
                            codOrderSummery.putExtra("address_id",addressId);
                            startActivity(codOrderSummery);
                            finish();
                        }else { validPinCodeAlert(); }
                    }else { Toast.makeText(this, "Please Select Address", Toast.LENGTH_SHORT).show(); }
                    Log.d(TAG, "payCod: "+addressId);
                }else {loginDialog.loginDialog(PaymentOptionActivity.this);}
            }else {Toast.makeText(PaymentOptionActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
        }catch (Exception e){e.printStackTrace();}
    }

    public void payOnline(View view){
        try{
            //String addressId = checkoutAddressListAdapter.getAddressId();
            if (networkState.isNetworkAvailable(PaymentOptionActivity.this)){
                if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                    if (addressId != null){
                        if (deliveryPinCodeList.contains(customerPostalCode.getText().toString().trim())){
//                            Log.d(TAG, "payCod: "+addressId);
//                            Intent payOnlineOrderSummery = new Intent(PaymentOptionActivity.this,CodOrderSummeryActivity.class);
//                            payOnlineOrderSummery.putExtra("payment_method","onlinePayment");
//                            payOnlineOrderSummery.putExtra("placeOrderPaymentType","Pay Online");
//                            payOnlineOrderSummery.putExtra("address_id",addressId);
//                            startActivity(payOnlineOrderSummery);
//                            finish();
                        Toast.makeText(PaymentOptionActivity.this, "Currently we are not serving Online Payment.", Toast.LENGTH_LONG).show();
                        }else { validPinCodeAlert(); }
                    }else { Toast.makeText(this, "Please Select Address", Toast.LENGTH_SHORT).show(); }
                }else {loginDialog.loginDialog(PaymentOptionActivity.this);}
            }else {Toast.makeText(PaymentOptionActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
        }catch (Exception e){e.printStackTrace();}
    }

    private void validPinCodeAlert(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delivery_alert_dialog_layout);
        TextView message_tv = dialog.findViewById(R.id.message_tv);
        message_tv.setText("We are delivering only in Jhargram containing this Pincode area - 721507");
        dialog.findViewById(R.id.removeCartItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //try { dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); }catch (Exception e){e.printStackTrace();}
        dialog.show();

//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Delivery Alert");
//        alertDialogBuilder.setMessage("We are currently not able to deliver at this location. We are delivering only this pin code area - 834001, 834002, 834003, 834004, 834008");
//        alertDialogBuilder.setCancelable(false);
//        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int arg1) {
//                dialogInterface.dismiss();
//            }
//        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
    }
}
