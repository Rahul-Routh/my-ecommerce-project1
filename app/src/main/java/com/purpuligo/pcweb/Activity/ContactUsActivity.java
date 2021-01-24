package com.purpuligo.pcweb.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.purpuligo.pcweb.Constants.Constants;
import com.purpuligo.pcweb.Global.LoginDialog;
import com.purpuligo.pcweb.Global.NetworkState;
import com.purpuligo.pcweb.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactUsActivity extends AppCompatActivity {

    @BindView(R.id.customerMobileNo) TextView customerMobileNo;
    @BindView(R.id.customerEmail) TextView customerEmail;
    @BindView(R.id.backBtn) ImageView backBtn;
    @BindView(R.id.count_cart) TextView count_cart;
    @BindView(R.id.count_wishList) TextView count_wishList;

    private NetworkState networkState;
    private LoginDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
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

        customerMobileNo.setText("03221-20164");
        customerEmail.setText("pcweb@gmail.com");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void cart(View view){
        startActivity(new Intent(ContactUsActivity.this,CartActivity.class)
                .putExtra("CartActivity","ProfileActivity"));
        finish();
    }

    public void wishList(View view){
        startActivity(new Intent(ContactUsActivity.this,WishListActivity.class));
        finish();
    }
}
