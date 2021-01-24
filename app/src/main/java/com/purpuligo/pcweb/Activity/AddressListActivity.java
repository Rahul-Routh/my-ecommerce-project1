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
import com.purpuligo.pcweb.Model.Adapter.AddressListAdapter;
import com.purpuligo.pcweb.Model.AddressListInteractorImpl;
import com.purpuligo.pcweb.Model.Pojo.AddressDetails;
import com.purpuligo.pcweb.Presenter.AddressListPresenter;
import com.purpuligo.pcweb.R;
import com.purpuligo.pcweb.View.AddressListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressListActivity extends AppCompatActivity implements AddressListView {

    private String TAG = "AddressListActivity";
    @BindView(R.id.addressListRecyclerView) RecyclerView addressListRecyclerView;
    @BindView(R.id.backBtn) ImageButton backBtn;
    @BindView(R.id.addAddressDetails) LinearLayout addAddressDetails;
    private String customer_email;
    private String session_data;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private NetworkState networkState;
    private LoginDialog loginDialog;
    private AddressListPresenter addressListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        addressListPresenter = new AddressListInteractorImpl(this);

        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
        session_data = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_SESSION_DATA,"");
        Log.d(TAG, "onCreate: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,""));

        if (networkState.isNetworkAvailable(this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                addressListPresenter.fetchAddressListFromServer(customer_email);
            }else {loginDialog.loginDialog(this);}
        }else {
            Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addAddressDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkState.isNetworkAvailable(AddressListActivity.this)){
                    if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                        startActivity(new Intent(AddressListActivity.this,CustomerDetailsActivity.class)
                                .putExtra("previousActivityValue","AddressListActivity"));
                    }else {loginDialog.loginDialog(AddressListActivity.this);}
                }else {Toast.makeText(AddressListActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
            }
        });
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading.....");
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
            addressListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            addressListRecyclerView.setAdapter(new AddressListAdapter(this,addressList));
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

    @Override
    public void removeAddressSuccess() {
        Toast.makeText(this, "Address Deleted", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,AddressListActivity.class));
        finish();
    }

    @Override
    public void removeAddressFailure() {
        Toast.makeText(this, "Address Delete Failure.", Toast.LENGTH_SHORT).show();
    }
}
