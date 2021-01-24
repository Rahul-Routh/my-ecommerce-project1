package com.purpuligo.pcweb.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.purpuligo.pcweb.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyPolicyActivity extends AppCompatActivity {

    //@BindView(R.id.cartBtn) ImageView cartBtn;
    //@BindView(R.id.wishListBtn) ImageView wishListBtn;
    @BindView(R.id.backBtn) ImageView backBtn;
    @BindView(R.id.subHeading) TextView subHeading;
    @BindView(R.id.subHeadingTwo) TextView subHeadingTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.bind(this);

//        cartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(PrivacyPolicyActivity.this,CartActivity.class));
//            }
//        });
//
//        wishListBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(PrivacyPolicyActivity.this,WishListActivity.class));
//            }
//        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        try {
            // get our html content
            String htmlAsString1 = getString(R.string.aajeevikaPrivacyPolicyList);
            Spanned htmlAsSpanned1 = Html.fromHtml(htmlAsString1);
            subHeading.setText(htmlAsSpanned1);

            // get our html content
//            String htmlAsString1 = getString(R.string.privacyPolicyListTwo);
//            Spanned htmlAsSpanned1 = Html.fromHtml(htmlAsString1);
//            subHeadingTwo.setText(htmlAsSpanned1);
        }catch (Exception e){e.printStackTrace();}
    }
}
