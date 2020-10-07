package com.purpuligo.ajeevikafarmfresh.Activity;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.WishListAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.WishListItems;
import com.purpuligo.ajeevikafarmfresh.Model.WishListInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.WishListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WishListActivity extends AppCompatActivity implements WishListView {

    private String TAG = "WishListActivity";
    @BindView(R.id.wishListRecyclerView) RecyclerView wishListRecyclerView;
    @BindView(R.id.emptyListIndicator) ImageView emptyListIndicator;
    @BindView(R.id.emptyListIndicator1) TextView emptyListIndicator1;
    @BindView(R.id.backBtn) ImageButton backBtn;
    @BindView(R.id.count_cart) TextView count_cart;
    private WishListInteractorImpl wishListInteractor;
    private ProgressDialog progressDialog;
    private NetworkState networkState;
    private WishListAdapter wishListAdapter;
    private String customer_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        ButterKnife.bind(this);
        networkState = new NetworkState();

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
        Log.d(TAG, "onCreate: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,""));

        if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CART_COUNT,"").equalsIgnoreCase("0")){
                count_cart.setVisibility(View.GONE);
            }else {count_cart.setText(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CART_COUNT,""));}
        }else {
            count_cart.setVisibility(View.GONE);
        }

        wishListInteractor = new WishListInteractorImpl(this);
        if (networkState.isNetworkAvailable(this)){
            if (customer_email.length()>0){
                wishListInteractor.fetchWishListDataFromServer(customer_email);
            }else {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(WishListActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("customer_name","Username");
        startActivity(intent);
        WishListActivity.this.finish();
    }

    public void cart(View view){
        startActivity(new Intent(WishListActivity.this,CartActivity.class)
                .putExtra("CartActivity","WishListActivity"));
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
    public void showWishList(ArrayList<WishListItems> wishListArray) {
        wishListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            Log.d(TAG, "showWishList: "+wishListArray.toString());
            wishListAdapter = new WishListAdapter(this,wishListArray);
            wishListRecyclerView.setAdapter(wishListAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showWishListError() {
        wishListRecyclerView.setVisibility(View.INVISIBLE);
        emptyListIndicator.setVisibility(View.VISIBLE);
        emptyListIndicator1.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRemovedProductFromWishList(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRemovedProductFromWishListError() {
        Toast.makeText(this, "Something happen wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showServerError() {
        Snackbar.make(backBtn,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }
}
