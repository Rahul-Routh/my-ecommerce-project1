package com.purpuligo.ajeevikafarmfresh.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.LoginDialog;
import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.CategoryListAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductCategoriesDetails;
import com.purpuligo.ajeevikafarmfresh.Model.SubCategoryInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.Presenter.SubCategoryPresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.SubCategoryView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubCategoryActivity extends AppCompatActivity implements SubCategoryView {

    private String TAG = "SubCategoryActivity";
    @BindView(R.id.subCategoryRecyclerView) RecyclerView subCategoryRecyclerView;
    @BindView(R.id.backBtn) ImageView backBtn;
    @BindView(R.id.emptyListIndicator) ImageView emptyListIndicator;
    @BindView(R.id.emptyListIndicator1) TextView emptyListIndicator1;
    @BindView(R.id.count_cart) TextView count_cart;
    @BindView(R.id.count_wishList) TextView count_wishList;
    @BindView(R.id.sub_category_searchView) androidx.appcompat.widget.SearchView sub_category_searchView;

    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private NetworkState networkState;
    private SubCategoryPresenter subCategoryPresenter;
    private LoginDialog loginDialog;
    private CategoryListAdapter categoryListAdapter;
    private String parent_position,category_id,sub_category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        subCategoryPresenter = new SubCategoryInteractorImpl(this);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);

        updateBadgeCount();

        Intent intent = getIntent();
        category_id = intent.getStringExtra("category_id");
        parent_position = intent.getStringExtra("parent_position");
        Log.d(TAG, "onCreate: "+category_id);

        if (networkState.isNetworkAvailable(this)){
            subCategoryPresenter.fetchCategoryListFromServer(Integer.parseInt(category_id));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SubCategoryActivity.this,CategoryListActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("parent_position",parent_position);
        startActivity(intent);
        SubCategoryActivity.this.finish();
    }

    private void updateBadgeCount(){
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
    }

    private void searchName(){
        try {
            sub_category_searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try { categoryListAdapter.filter(newText); }catch (Exception e){e.printStackTrace();}
                    return true;
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    public void cart(View view){
        if (networkState.isNetworkAvailable(SubCategoryActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                subCategoryPresenter.fetchCartDataFromServer();
            }else {loginDialog.loginDialog(SubCategoryActivity.this);}
        }else {Toast.makeText(SubCategoryActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
    }

    public void wishList(View view){
        if (networkState.isNetworkAvailable(SubCategoryActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                subCategoryPresenter.fetchWishListDataFromServer();
            }else {loginDialog.loginDialog(SubCategoryActivity.this);}
        }else {Toast.makeText(SubCategoryActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
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
    public void setProductListSuccess(ArrayList<ProductCategoriesDetails> categoryListArray) {
        try {
            if (categoryListArray.isEmpty()){
                subCategoryRecyclerView.setVisibility(View.INVISIBLE);
                emptyListIndicator.setVisibility(View.VISIBLE);
                emptyListIndicator1.setVisibility(View.VISIBLE);
            }else {
                Log.d(TAG, "setProductListSuccess: "+categoryListArray);
                //subCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                subCategoryRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
                categoryListAdapter = new CategoryListAdapter(this,categoryListArray,parent_position,"SubCategoryActivity");
                subCategoryRecyclerView.setAdapter(categoryListAdapter);
                subCategoryRecyclerView.setNestedScrollingEnabled(false);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void setProductListError() {
        subCategoryRecyclerView.setVisibility(View.INVISIBLE);
        emptyListIndicator.setVisibility(View.VISIBLE);
        emptyListIndicator1.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToWishList() {
        startActivity(new Intent(SubCategoryActivity.this,WishListActivity.class));
        finish();
    }

    @Override
    public void navigateToCart() {
        startActivity(new Intent(SubCategoryActivity.this,CartActivity.class)
                .putExtra("CartActivity","ProductListActivity")
                .putExtra("category_id",parent_position));
        finish();
    }

    @Override
    public void showServerError() {
        Snackbar.make(backBtn,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }
}
