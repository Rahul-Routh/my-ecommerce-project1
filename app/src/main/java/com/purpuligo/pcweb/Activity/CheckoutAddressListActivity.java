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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.purpuligo.pcweb.Constants.Constants;
import com.purpuligo.pcweb.Global.LoginDialog;
import com.purpuligo.pcweb.Global.NetworkState;
import com.purpuligo.pcweb.Model.Adapter.CheckoutAddressListAdapter;
import com.purpuligo.pcweb.Model.CheckoutAddressListInteractorImpl;
import com.purpuligo.pcweb.Model.Pojo.AddressDetails;
import com.purpuligo.pcweb.Presenter.CheckoutAddressListPresenter;
import com.purpuligo.pcweb.R;
import com.purpuligo.pcweb.View.CheckoutAddressListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckoutAddressListActivity extends AppCompatActivity implements CheckoutAddressListView{

    private String TAG = "CheckoutAddressListActivity";
    @BindView(R.id.backBtn) ImageButton backBtn;
    @BindView(R.id.addAddressDetails) LinearLayout addAddressDetails;
    @BindView(R.id.addressListRecyclerView) RecyclerView addressListRecyclerView;
    private String totalPrice;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private NetworkState networkState;
    private LoginDialog loginDialog;
    private CheckoutAddressListPresenter checkoutAddressListPresenter;
    private CheckoutAddressListAdapter checkoutAddressListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_address_list);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        checkoutAddressListPresenter = new CheckoutAddressListInteractorImpl(this);
        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);

        Intent intent = getIntent();
        totalPrice = intent.getStringExtra("totalPrice");

        if (networkState.isNetworkAvailable(this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                checkoutAddressListPresenter.fetchAddressListFromServer(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,""));
            }else {loginDialog.loginDialog(this);}
        }else {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();}

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addAddressDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkState.isNetworkAvailable(CheckoutAddressListActivity.this)){
                    if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                        startActivity(new Intent(CheckoutAddressListActivity.this,CustomerDetailsActivity.class)
                                .putExtra("previousActivityValue","CheckoutAddressListActivity"));
                        finish();
                    }else {loginDialog.loginDialog(CheckoutAddressListActivity.this);}
                }else {
                    Toast.makeText(CheckoutAddressListActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
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
    public void showAddressList(ArrayList<AddressDetails> addressList) {
        try {
            Log.d(TAG, "showAddressList: "+addressList);
            addressListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            checkoutAddressListAdapter = new CheckoutAddressListAdapter(this,addressList);
            addressListRecyclerView.setAdapter(checkoutAddressListAdapter);
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showAddressListError() {
        Log.d(TAG, "showAddressListError: ");
    }

    @Override
    public void showServerError() {
        Snackbar.make(addressListRecyclerView,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }

    public void selectedAddress(View view){
        String addressId = checkoutAddressListAdapter.showAddressId();
        String firstName = checkoutAddressListAdapter.showFirstName();
        String lastName = checkoutAddressListAdapter.showLastName();
        String address = checkoutAddressListAdapter.showAddress();
        String landmark = checkoutAddressListAdapter.showLandmark();
        String city = checkoutAddressListAdapter.showCity();
        String state = checkoutAddressListAdapter.showState();
        String postalCode = checkoutAddressListAdapter.showPostalCode();
        String mobile = checkoutAddressListAdapter.showMobile();

        if (checkoutAddressListAdapter.showAddressId() != null){
            Toast.makeText(this, "Address Selected", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,PaymentOptionActivity.class);
            intent.putExtra("addressId",addressId);
            intent.putExtra("firstName",firstName);
            intent.putExtra("lastName",lastName);
            intent.putExtra("address",address);
            intent.putExtra("landmark",landmark);
            intent.putExtra("city",city);
            intent.putExtra("state",state);
            intent.putExtra("postalCode",postalCode);
            intent.putExtra("mobile",mobile);
            intent.putExtra("totalPrice",totalPrice);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this, "Please Select Address", Toast.LENGTH_SHORT).show();
        }
    }
}
