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

public class AboutUsActivity extends AppCompatActivity {

    //@BindView(R.id.cartBtn) ImageView cartBtn;
    //@BindView(R.id.wishListBtn) ImageView wishListBtn;
    @BindView(R.id.aboutUsText) TextView aboutUsText;
    @BindView(R.id.backBtn) ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

//        cartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(AboutUsActivity.this,CartActivity.class));
//            }
//        });
//
//        wishListBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(AboutUsActivity.this,WishListActivity.class));
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
            String htmlAsString = getString(R.string.aboutUsText);
            Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);
            aboutUsText.setText(htmlAsSpanned);

        }catch (Exception e){e.printStackTrace();}
    }
}
