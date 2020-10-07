package com.purpuligo.ajeevikafarmfresh.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.LoginDialog;
import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

//    @BindView(R.id.profilePic) ImageView profilePic;
    @BindView(R.id.customerName) TextView customerName;
    @BindView(R.id.customerEmail) TextView customerEmail;
    @BindView(R.id.customerAddress) TextView customerAddress;
    @BindView(R.id.customerCity) TextView customerCity;
    @BindView(R.id.customerState) TextView customerState;
    @BindView(R.id.customerPostalCode) TextView customerPostalCode;
    @BindView(R.id.forgotPassword) TextView forgotPassword;
    @BindView(R.id.backBtn) ImageView backBtn;
    @BindView(R.id.count_cart) TextView count_cart;
    @BindView(R.id.count_wishList) TextView count_wishList;

    private NetworkState networkState;
    private LoginDialog loginDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();

        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        final String customer_name = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,"");
        final String customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");

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

        if (networkState.isNetworkAvailable(this)){
            if (customer_name.length()>0 && customer_email.length()>0){

                customerName.setText(customer_name);
                customerEmail.setText(customer_email);
                customerAddress.setText(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_ADDRESS_ONE,"")+", "+
                        sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_ADDRESS_TWO,""));
                customerCity.setText(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CITY,""));
                customerState.setText(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_STATE,""));
                customerPostalCode.setText(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_POSTAl_CODE,""));
            }else {loginDialog.loginDialog(this);}
        }else {Toast.makeText(this, "Check Connection", Toast.LENGTH_SHORT).show();}

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkState.isNetworkAvailable(ProfileActivity.this)){
                    if (customer_email.length()>0){
                        startActivity(new Intent(ProfileActivity.this,ForgotPasswordActivity.class));
                    }else {loginDialog.loginDialog(ProfileActivity.this);}
                }else {Toast.makeText(ProfileActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
            }
        });
    }

    public void cart(View view){
        startActivity(new Intent(ProfileActivity.this,CartActivity.class)
                .putExtra("CartActivity","ProfileActivity"));
        finish();
    }

    public void wishList(View view){
        startActivity(new Intent(ProfileActivity.this,WishListActivity.class));
        finish();
    }
}
