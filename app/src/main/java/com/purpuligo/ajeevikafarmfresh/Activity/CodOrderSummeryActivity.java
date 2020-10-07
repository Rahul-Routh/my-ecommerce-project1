package com.purpuligo.ajeevikafarmfresh.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.LoginDialog;
import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.Global.UserSessionManager;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.CheckoutPriceListAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.CodOrderSummeryAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.CodOrderSummeryInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartItemList;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartPriceDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.CodOrderSummeryPresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.CodOrderSummeryView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
//import instamojo.library.InstamojoPay;
//import instamojo.library.InstapayListener;

public class CodOrderSummeryActivity extends AppCompatActivity implements CodOrderSummeryView{

    private String TAG = "CodOrderSummeryActivity";
    @BindView(R.id.codOrderSummeryRecyclerView) RecyclerView codOrderSummeryRecyclerView;
    @BindView(R.id.checkoutPriceDetails) RecyclerView checkoutPriceDetails;
    @BindView(R.id.backBtn) ImageButton backBtn;
    @BindView(R.id.customerFirstName) TextView customerFirstName;
    @BindView(R.id.customerLastName) TextView customerLastName;
    @BindView(R.id.customerAddress) TextView customerAddress;
    //@BindView(R.id.customerLandmark) TextView customerLandmark;
    @BindView(R.id.customerState) TextView customerState;
    @BindView(R.id.customerPostalCode) TextView customerPostalCode;
    @BindView(R.id.customerMobile) TextView customerMobile;
    @BindView(R.id.customerCity) TextView customerCity;
    @BindView(R.id.convenienceChargeTitle) TextView convenienceChargeTitle;
    @BindView(R.id.convenienceChargeAmount) TextView convenienceChargeAmount;
    @BindView(R.id.totalInclConvenience) TextView totalInclConvenience;
    @BindView(R.id.placeOrderBtn) Button placeOrderBtn;

    private boolean stock = false;
    private String customer_email;
    private String session_data;
    private String address_id;
    private String payment_method;
    private String order_type;
    private String mobile_number;
    private Double totalPlusConvenience;
    private String forPaymentTotal;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private NetworkState networkState;
    private LoginDialog loginDialog;
    private CodOrderSummeryPresenter codOrderSummeryPresenter;
    private UserSessionManager userSessionManager;
    private CheckoutPriceListAdapter checkoutPriceListAdapter;
    
    //------------instamojo-----------
    //private InstamojoPay instamojoPay;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cod_order_summery);
        // Call the function callInstamojo to start payment here
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        userSessionManager = new UserSessionManager(this);
        codOrderSummeryPresenter = new CodOrderSummeryInteractorImpl(this);

        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
        session_data = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_SESSION_DATA,"");
        Log.d(TAG, "onCreate: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,""));

        Intent intent = getIntent();
        try {
            address_id = intent.getStringExtra("address_id");
            payment_method = intent.getStringExtra("payment_method");
            order_type = intent.getStringExtra("placeOrderPaymentType");
            placeOrderBtn.setText(order_type);
        }catch (Exception e){e.printStackTrace();}

        if (networkState.isNetworkAvailable(this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                codOrderSummeryPresenter.getCheckoutDetails(address_id,customer_email,session_data);
            }else {loginDialog.loginDialog(this);}
        }else {
            Toast.makeText(CodOrderSummeryActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}

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
    public void showFirstName(String firstName) {
        try {
            customerFirstName.setText(firstName);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showFirstNameError() {
        Log.d(TAG, "showFirstNameError: ");
    }

    @Override
    public void showLastName(String lastName) {
        try {
            customerLastName.setText(lastName);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showLastNameError() {
        Log.d(TAG, "showLastNameError: ");
    }

    @Override
    public void showAddress(String address) {
        try {
            customerAddress.setText(address);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showAddressError() {
        Log.d(TAG, "showAddressError: ");
    }

    @Override
    public void showLandmark(String landmark) {
        try {
            Log.d(TAG, "showLandmark: "+landmark);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showLandmarkError() {
        Log.d(TAG, "showLandmarkError: ");
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
        Log.d(TAG, "showCityError: ");
    }

    @Override
    public void showState(String state) {
        try {
            customerState.setText(state);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showStateError() {
        Log.d(TAG, "showStateError: ");
    }

    @Override
    public void showPostalCode(String postalCode) {
        try {
            customerPostalCode.setText(postalCode);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showPostalCodeError() {
        Log.d(TAG, "showPostalCodeError: ");
    }

    @Override
    public void showMobile(String mobileNumber) {
        try {
            mobile_number = mobileNumber;
            customerMobile.setText(mobileNumber);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showMobileError() {
        Log.d(TAG, "showMobileError: ");
    }

    @Override
    public void showProductDetailsList(ArrayList<CartItemList> cartItemArrayList) {
        try {
            Log.d(TAG, "showProductDetailsList: "+cartItemArrayList);
            codOrderSummeryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            codOrderSummeryRecyclerView.setAdapter(new CodOrderSummeryAdapter(this,cartItemArrayList));
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showProductDetailsListError() {
        Log.d(TAG, "showProductDetailsListError: ");
    }

    @Override
    public void showPaymentDetails(ArrayList<CartPriceDetails> checkoutPriceList, String extra_charge_name, String extra_charge_value) {
        try {
            Log.d(TAG, "showPaymentDetails: "+checkoutPriceList);
            checkoutPriceDetails.setLayoutManager(new LinearLayoutManager(this));
            checkoutPriceListAdapter = new CheckoutPriceListAdapter(this,checkoutPriceList);
            checkoutPriceDetails.setAdapter(checkoutPriceListAdapter);
            //checkoutPriceDetails.setAdapter(new CartPriceDetailsAdapter(this,checkoutPriceList));

            String rawData = checkoutPriceList.get(checkoutPriceList.size()-1).getPrice_amount();
            String convenience = rawData.replace("Rs.","").replace(",","");
            try {
                convenienceChargeTitle.setText(extra_charge_name);
            }catch (Exception e){e.printStackTrace();}
            if (extra_charge_value != null) {
                //Double convenienceCharge = (2.5*Double.parseDouble(convenience))/100;
                //if (Double.parseDouble(convenience) <= 250.0){
                //Double convenienceCharge = 50.00;
                Double convenienceCharge = Double.parseDouble(extra_charge_value);
                Log.d(TAG, "showPaymentDetails 1: " + convenience);
                Log.d(TAG, "showPaymentDetails 2: " + Double.parseDouble(convenience));
                Log.d(TAG, "showPaymentDetails 3: " + convenienceCharge);
                Log.d(TAG, "showPaymentDetails 4: " + String.format("%.2f", Double.parseDouble(String.valueOf(convenienceCharge))));
                convenienceChargeAmount.setText("Rs." + String.format("%.2f", convenienceCharge));
                Double totalPlusConvenience = convenienceCharge + Double.parseDouble(convenience);
                totalInclConvenience.setText("Rs." + String.format("%.2f", totalPlusConvenience));
//            }else {
//                Double convenienceCharge = 50.00;
//                Log.d(TAG, "showPaymentDetails 1: " + convenience);
//                Log.d(TAG, "showPaymentDetails 2: " + Double.parseDouble(convenience));
//                Log.d(TAG, "showPaymentDetails 3: " + convenienceCharge);
//                Log.d(TAG, "showPaymentDetails 4: " + String.format("%.2f", Double.parseDouble(String.valueOf(convenienceCharge))));
//                convenienceChargeAmount.setText("Rs." + String.format("%.2f", convenienceCharge));
//                totalPlusConvenience = convenienceCharge + Double.parseDouble(convenience);
//                forPaymentTotal = String.format("%.2f", totalPlusConvenience);
//                totalInclConvenience.setText("Rs." + String.format("%.2f", totalPlusConvenience));
//            }
            }
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showPaymentDetailsError() {

    }

    @Override
    public void showDeliveryTime() {
        
    }

    @Override
    public void showDeliveryTimeError() {

    }

    @Override
    public void showSubmitOrderSuccess() {

    }

    @Override
    public void showSubmitOrderError() {

    }

    @Override
    public void setCartCount() {
        try {
            int cartCount = 0;
            Log.d(TAG, "setCartCount: "+cartCount);
            userSessionManager.updateCartCount("0");
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void setCartCountError() {
        Log.d(TAG, "setCartCountError: ");
    }

    @Override
    public void showOrderPlacedMessage(String message, String order_id) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,OrderPlacedActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("order_id",order_id);
            startActivity(intent);
            CodOrderSummeryActivity.this.finish();
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showOrderPlacedFilure(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }catch (Exception e){e.printStackTrace();}
    }

    public void submitOrder(View view){
        if (networkState.isNetworkAvailable(this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                if (mobile_number.length() > 9){
                    removeAlert();
                }else {
                    Toast.makeText(this, "Please Select Valid Mobile Number.", Toast.LENGTH_LONG).show();
                }
            }else {loginDialog.loginDialog(this);}
        }else {
            Toast.makeText(CodOrderSummeryActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
    }

    @Override
    public void showServerError() {
        Snackbar.make(codOrderSummeryRecyclerView,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showStock(boolean stock) {
        try {
            this.stock = stock;
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showStockError() {
        stock = false;
    }

    private void removeAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Submit Order");
        alertDialogBuilder.setMessage("Are you really want to submit order.");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (networkState.isNetworkAvailable(CodOrderSummeryActivity.this)){
                    try {
                        if (payment_method.equalsIgnoreCase("codPayment")){
                            if (stock) {
                                codOrderSummeryPresenter.submitCodOrder(customer_email, session_data, (int) Double.parseDouble(address_id));
                            }else {
                                Toast.makeText(CodOrderSummeryActivity.this, "Product out of stock now.", Toast.LENGTH_SHORT).show();
                            }
                        }else if (payment_method.equalsIgnoreCase("onlinePayment")){
                            Log.d(TAG, "removeAlert: "+mobile_number);
//                            callInstamojoPay(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,""),
//                                    mobile_number,forPaymentTotal,"Murighonto",
//                                    sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,""));
                            Toast.makeText(CodOrderSummeryActivity.this, "Currently we are not serving Online Payment.", Toast.LENGTH_SHORT).show();
                        } }catch (Exception e){e.printStackTrace();}
                }else { Toast.makeText(CodOrderSummeryActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //---------------------------------instamojo payment gateway--------------------------------------------
//    InstapayListener listener;
//    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
//        try {
//            final Activity activity = this;
//            instamojoPay = new InstamojoPay();
//            filter = new IntentFilter("ai.devsupport.instamojo");
//            registerReceiver(instamojoPay, filter);
//            JSONObject pay = new JSONObject();
//            pay.put("email", email);
//            pay.put("phone", phone);
//            pay.put("purpose", purpose);
//            pay.put("amount", amount);
//            pay.put("name", buyername);
//            pay.put("send_sms", true);
//            pay.put("send_email", true);
//
//            initListener();
//            instamojoPay.start(activity, pay, listener);
//        } catch (JSONException e) {
//            unregisterReceiver(instamojoPay);
//            e.printStackTrace();
//        }
//    }
//
//    private void initListener() {
//        listener = new InstapayListener() {
//            @Override
//            public void onSuccess(String response) {
//                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
//                Log.d(TAG, "onPaymentSuccess: "+response);
//                try{
//                    unregisterReceiver(instamojoPay);
//                    String responseArray[] = response.split(":");
//                    String payment_id = responseArray[3].substring(responseArray[3].indexOf("=")+1);
//                    Log.d(TAG, "onSuccess payment_id: "+payment_id);
//                    codOrderSummeryPresenter.submitOnlinePaymentOrder(customer_email,session_data,payment_id,(int)Double.parseDouble(address_id));
//                }catch (Exception e){e.printStackTrace();}
//            }
//
//            @Override
//            public void onFailure(int code, String reason) {
//                //Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG).show();
//                Log.d(TAG, "onPaymentFailure: "+reason);
//                showOrderPlacedFilure(reason);
//            }
//        };
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        unregisterReceiver(instamojoPay);
//    }
}
