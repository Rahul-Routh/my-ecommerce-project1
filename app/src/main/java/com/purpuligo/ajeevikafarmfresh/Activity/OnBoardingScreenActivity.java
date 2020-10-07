package com.purpuligo.ajeevikafarmfresh.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purpuligo.ajeevikafarmfresh.Model.Adapter.OnBoardingScreenAdapter;
import com.purpuligo.ajeevikafarmfresh.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnBoardingScreenActivity extends AppCompatActivity {

    @BindView(R.id.onBoardViewPager) ViewPager onBoardViewPager;
    @BindView(R.id.mDotLayout) LinearLayout mDotLayout;
    @BindView(R.id.mNextBtn) Button mNextBtn;
    @BindView(R.id.mBackBtn) Button mBackBtn;
    @BindView(R.id.skipBtn) Button skipBtn;

    private String[] slideHeading = {"Dishes", "Fast Food", "Ingredients"};
    private TextView[] mDots;
    private int mCurrentPage;
    private OnBoardingScreenAdapter onBoardingScreenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);
        ButterKnife.bind(this);

        onBoardingScreenAdapter = new OnBoardingScreenAdapter(this,slideHeading);
        onBoardViewPager.setAdapter(onBoardingScreenAdapter);
        onBoardViewPager.setOffscreenPageLimit(slideHeading.length);

        addDotsIndicator(0);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNextBtn.getText().toString().equalsIgnoreCase("Start")){
                    Intent intent = new Intent(OnBoardingScreenActivity.this,HomeActivity.class);
                    intent.putExtra("customer_name","Guest");
                    startActivity(intent);
                    finish();
                }else {
                    onBoardViewPager.setCurrentItem(mCurrentPage + 1);
                }
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBoardViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnBoardingScreenActivity.this,HomeActivity.class);
                intent.putExtra("customer_name","Guest");
                startActivity(intent);
                finish();
            }
        });

        onBoardViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                addDotsIndicator(position);
                mCurrentPage = position;

                if (position == 0) {
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(false);
                    mBackBtn.setVisibility(View.INVISIBLE);

                    mNextBtn.setText("Next");
                    mBackBtn.setText("");
                }else if (position == mDots.length-1) {
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(true);
                    mBackBtn.setVisibility(View.VISIBLE);

                    mNextBtn.setText("Start");
                    mBackBtn.setText("Back");
                }else {
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(true);
                    mBackBtn.setVisibility(View.VISIBLE);

                    mNextBtn.setText("Next");
                    mBackBtn.setText("Back");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for (int i=0; i<mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }
}
