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

public class TermAndConditionActivity extends AppCompatActivity {

    //@BindView(R.id.cartBtn) ImageView cartBtn;
    //@BindView(R.id.wishListBtn) ImageView wishListBtn;
    @BindView(R.id.backBtn) ImageView backBtn;
    @BindView(R.id.subHeading) TextView subHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);
        ButterKnife.bind(this);

//        cartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(TermAndConditionActivity.this,CartActivity.class));
//            }
//        });
//
//        wishListBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(TermAndConditionActivity.this,WishListActivity.class));
//            }
//        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // get our html content
        try {
            String htmlAsString = getString(R.string.termsAndConditions);
            Spanned htmlAsSpanned = Html.fromHtml(htmlAsString); // used by TextView
            subHeading.setText(htmlAsSpanned);
        }catch (Exception e){e.printStackTrace();}
    }
}
