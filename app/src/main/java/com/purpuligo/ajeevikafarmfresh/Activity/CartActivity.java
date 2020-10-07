package com.purpuligo.ajeevikafarmfresh.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.LoginDialog;
import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.CartAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.CartPriceDetailsAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.CartInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartItemList;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartPriceDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.CartPresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.CartView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements CartView {

    private String TAG = "CartActivity";
    @BindView(R.id.cartRecyclerView) RecyclerView cartRecyclerView;
    @BindView(R.id.cartDetailsLayout) LinearLayout cartDetailsLayout;
    @BindView(R.id.emptyCartShowLayout) LinearLayout emptyCartShowLayout;
    @BindView(R.id.backBtn) ImageButton backBtn;
    @BindView(R.id.cartToolBar) Toolbar cartToolBar;
    //@BindView(R.id.emptyListIndicator) ImageView emptyListIndicator;
    //@BindView(R.id.emptyListIndicator1) TextView emptyListIndicator1;
    @BindView(R.id.convenienceChargeLayout) TableLayout convenienceChargeLayout;
    //@BindView(R.id.placeOrder) Button placeOrder;
    @BindView(R.id.cartPriceDetails) RecyclerView cartPriceDetails;
    @BindView(R.id.count_wishList) TextView count_wishList;
    //@BindView(R.id.convenienceChargeAmount) TextView convenienceChargeAmount;
    @BindView(R.id.totalInclConvenience) TextView totalInclConvenience;
    @BindView(R.id.cartView) View cartView;
    @BindView(R.id.priceView) View priceView;
    @BindView(R.id.placeOrder) Button placeOrder;

    private String customer_email;
    private String session_data;
    private String totalPrice;
    private ProgressDialog progressDialog;
    private NetworkState networkState;
    private LoginDialog loginDialog;
    private CartPresenter cartPresenter;
    private CartPriceDetailsAdapter cartPriceDetailsAdapter;
    private String prevActivity;
    private String category_id;
    private String product_id;
    private String cartCount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        cartPresenter = new CartInteractorImpl(this);

        Intent intent = getIntent();
        prevActivity = intent.getStringExtra("CartActivity");
        category_id = intent.getStringExtra("category_id");
        product_id = intent.getStringExtra("product_id");

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
        session_data = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_SESSION_DATA,"");
        //Log.d(TAG, "onCreate: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,""));

        if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_WISHLIST_COUNT,"").equalsIgnoreCase("0")){
                count_wishList.setVisibility(View.GONE);
            }else {count_wishList.setText(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_WISHLIST_COUNT,""));}
        }else {
            count_wishList.setVisibility(View.GONE);
        }

        if (networkState.isNetworkAvailable(this)){
            if (customer_email.length()>0){
                cartPresenter.fetchCartDetailsFromServer(customer_email,session_data);
            }else {loginDialog.loginDialog(this);}
        }else {Toast.makeText(CartActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        placeOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(CartActivity.this,PaymentOptionActivity.class));
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CartActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("customer_name","Username");
        startActivity(intent);
        CartActivity.this.finish();
    }

    public void wishList(View view){
        if (networkState.isNetworkAvailable(CartActivity.this)){
            if (customer_email.length()>0){
                startActivity(new Intent(CartActivity.this,WishListActivity.class));
            }else {loginDialog.loginDialog(CartActivity.this);}
        }else {
            Toast.makeText(CartActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
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
    public void showCartItemList(ArrayList<CartItemList> cartItemArrayList) {
        try {
            Log.d(TAG, "showCartItemList: "+cartItemArrayList);
            cartDetailsLayout.setVisibility(View.VISIBLE);
            emptyCartShowLayout.setVisibility(View.GONE);
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            cartRecyclerView.setAdapter(new CartAdapter(this,cartItemArrayList));
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showCartItemListError(String count) {
        try{
            cartDetailsLayout.setVisibility(View.GONE);
            emptyCartShowLayout.setVisibility(View.VISIBLE);
            Log.d(TAG, "showCartItemListError: "+count);
            cartCount = count;
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showCartPriceDetailsList(ArrayList<CartPriceDetails> cartPriceList) {
        try {
            cartPriceDetails.setLayoutManager(new LinearLayoutManager(this));
            totalPrice = cartPriceList.get(cartPriceList.size()-1).getPrice_amount();
            cartPriceDetailsAdapter = new CartPriceDetailsAdapter(this,cartPriceList);
            cartPriceDetails.setAdapter(cartPriceDetailsAdapter);

            String rawData = cartPriceList.get(cartPriceList.size()-1).getPrice_amount();
            String convenience = rawData.replace("Rs.","").replace(",","");
            Log.d(TAG, "showCartPriceDetailsList: "+convenience);
            //Double convenienceCharge = (2.5*Double.parseDouble(convenience))/100;
            //if (Double.parseDouble(convenience) <= 250.0){
            if (Double.parseDouble(convenience) <= 25.0){
                Double convenienceCharge = 0.00;
                Log.d(TAG, "showPaymentDetails 1: "+convenience);
                Log.d(TAG, "showPaymentDetails 2: "+Double.parseDouble(convenience));
                Log.d(TAG, "showPaymentDetails 3: "+convenienceCharge);
                Log.d(TAG, "showPaymentDetails 4: "+String.format("%.2f",Double.parseDouble(String.valueOf(convenienceCharge))));
                //convenienceChargeAmount.setText("Rs."+String.format("%.2f",convenienceCharge));
                Double totalPlusConvenience = convenienceCharge+Double.parseDouble(convenience);
                totalInclConvenience.setText("Rs."+String.format("%.2f",totalPlusConvenience));
            }else {
                Double convenienceCharge = 0.00;
                Log.d(TAG, "showPaymentDetails 1: "+convenience);
                Log.d(TAG, "showPaymentDetails 2: "+Double.parseDouble(convenience));
                Log.d(TAG, "showPaymentDetails 3: "+convenienceCharge);
                Log.d(TAG, "showPaymentDetails 4: "+String.format("%.2f",Double.parseDouble(String.valueOf(convenienceCharge))));
                //convenienceChargeAmount.setText("Rs."+String.format("%.2f",convenienceCharge));
                Double totalPlusConvenience = convenienceCharge+Double.parseDouble(convenience);
                totalInclConvenience.setText("Rs."+String.format("%.2f",totalPlusConvenience));
            }
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showCartPriceDetailsListError() {
        Log.d(TAG, "showCartPriceDetailsListError: ");
    }

    @Override
    public void removeItemSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CartActivity.this,CartActivity.class)
                .putExtra("CartActivity",prevActivity)
                .putExtra("product_id",product_id)
                .putExtra("category_id",category_id));
        finish();
    }

    @Override
    public void showServerError() {
        Snackbar.make(cartRecyclerView,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }

    public void placeOrder(View view){
        if (networkState.isNetworkAvailable(this)){
            try{
                Log.d(TAG, "placeOrder: "+cartCount);
                if (customer_email.length()>0){
                    if (cartCount == null){
                        Toast.makeText(this, "Something went wrong please try again..", Toast.LENGTH_SHORT).show();
                    }else {
                        if (cartCount.equalsIgnoreCase("0")){
                            Toast.makeText(this, "Please Add Item in Cart", Toast.LENGTH_SHORT).show();
                        }else {
                            startActivity(new Intent(CartActivity.this,PaymentOptionActivity.class)
                                    .putExtra("totalPrice",totalPrice));
                        }
                    }
                }else {loginDialog.loginDialog(this);}
            }catch (Exception e){e.printStackTrace();}
        }else {Toast.makeText(CartActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
    }
}
