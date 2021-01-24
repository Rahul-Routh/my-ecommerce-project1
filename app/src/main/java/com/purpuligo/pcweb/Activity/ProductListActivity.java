package com.purpuligo.pcweb.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.pcweb.Constants.Constants;
import com.purpuligo.pcweb.Global.LoginDialog;
import com.purpuligo.pcweb.Global.NetworkState;
import com.purpuligo.pcweb.Model.Adapter.ProductListAdapter;
import com.purpuligo.pcweb.Model.Pojo.ProductListDetails;
import com.purpuligo.pcweb.Model.ProductListInteractorImpl;
import com.purpuligo.pcweb.Presenter.ProductListPresenter;
import com.purpuligo.pcweb.R;
import com.purpuligo.pcweb.View.ProductListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListActivity extends AppCompatActivity implements ProductListView {

    private String TAG = "ProductListActivity";
    @BindView(R.id.productListRecyclerView) RecyclerView productListRecyclerView;
    @BindView(R.id.backBtn) ImageView backBtn;
    @BindView(R.id.emptyListIndicator) ImageView emptyListIndicator;
    @BindView(R.id.emptyListIndicator1) TextView emptyListIndicator1;
    @BindView(R.id.count_cart) TextView count_cart;
    @BindView(R.id.count_wishList) TextView count_wishList;
    @BindView(R.id.product_list_searchView) SearchView product_list_searchView;

    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private ProductListPresenter productListPresenter;
    private NetworkState networkState;
    private LoginDialog loginDialog;
    private ProductListAdapter productListAdapter;
    private String category_id,parent_position,previousActivity,sub_category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        productListPresenter = new ProductListInteractorImpl(this);

        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        final String customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
        Log.d(TAG, "onCreate: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,""));

        if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CART_COUNT,"").equalsIgnoreCase("0")){
                count_cart.setVisibility(View.GONE);
            }else {count_cart.setText(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CART_COUNT,""));}
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_WISHLIST_COUNT,"").equalsIgnoreCase("0")){
                count_wishList.setVisibility(View.GONE);
            }else {count_wishList.setText(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_WISHLIST_COUNT,""));}
        }else {
            count_cart.setVisibility(View.GONE);
            count_wishList.setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        category_id = intent.getStringExtra("category_id");
        parent_position = intent.getStringExtra("parent_position");
        previousActivity = intent.getStringExtra("previousActivity");
        Log.d(TAG, "onCreate: "+previousActivity);

        if (networkState.isNetworkAvailable(this)){
            productListPresenter.fetchProductListDataFromServer(category_id);
        }else {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchName();
    }

    private void searchName(){
        try {
            product_list_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try { productListAdapter.filter(newText); }catch (NullPointerException e){e.printStackTrace();}
                    return true;
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onBackPressed() {
//        if (previousActivity.equalsIgnoreCase("SubCategoryActivity")){
//            super.onBackPressed();
//            Intent intent = new Intent(ProductListActivity.this,SubCategoryActivity.class);
//            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.putExtra("parent_position",parent_position);
//            intent.putExtra("category_id",category_id);
//            startActivity(intent);
//            ProductListActivity.this.finish();
//        }else if (previousActivity.equalsIgnoreCase("CategoryListActivity")){
            super.onBackPressed();
            Intent intent = new Intent(ProductListActivity.this,CategoryListActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("parent_position",parent_position);
            startActivity(intent);
            ProductListActivity.this.finish();
        //}
    }

    public void cart(View view){
        if (networkState.isNetworkAvailable(ProductListActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                productListPresenter.fetchCartDataFromServer();
            }else {loginDialog.loginDialog(ProductListActivity.this);}
        }else {Toast.makeText(ProductListActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
    }

    public void wishList(View view){
        if (networkState.isNetworkAvailable(ProductListActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                productListPresenter.fetchWishListDataFromServer();
            }else {loginDialog.loginDialog(ProductListActivity.this);}
        }else {Toast.makeText(ProductListActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
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
    public void setProductListSuccess(ArrayList<ProductListDetails> productListArray) {
        try {
            Log.d(TAG, "setProductListSuccess: "+productListArray.toString());
            if (productListArray.isEmpty()){
                productListRecyclerView.setVisibility(View.INVISIBLE);
                emptyListIndicator.setVisibility(View.VISIBLE);
                emptyListIndicator1.setVisibility(View.VISIBLE);
            }else {
                productListRecyclerView.setVisibility(View.VISIBLE);
                productListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                productListAdapter = new ProductListAdapter(this,productListArray,category_id,parent_position,"ProductListActivity","0");
                productListRecyclerView.setAdapter(productListAdapter);
                productListRecyclerView.setNestedScrollingEnabled(false);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void setProductListError() {
        productListRecyclerView.setVisibility(View.INVISIBLE);
        emptyListIndicator.setVisibility(View.VISIBLE);
        emptyListIndicator1.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToWishList() {
        startActivity(new Intent(ProductListActivity.this,WishListActivity.class));
        finish();
    }

    @Override
    public void navigateToCart() {
        startActivity(new Intent(ProductListActivity.this,CartActivity.class)
                .putExtra("CartActivity","ProductListActivity")
                .putExtra("category_id",category_id));
        finish();
    }

    @Override
    public void showServerError() {
        Snackbar.make(backBtn,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }
}
