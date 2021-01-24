package com.purpuligo.pcweb.Model.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purpuligo.pcweb.R;

public class OnBoardingScreenAdapter extends PagerAdapter {
    private Context context;
    private String[] slideHeading;

    //private int[] slideImages = {R.drawable.background,R.drawable.background_two,R.drawable.background_three};
    //private String[] slideHeading = {"Screen One", "Screen Two", "Screen Three"};

    private int[] slideImages = {R.drawable.chair,R.drawable.bed,R.drawable.laptop_table};


    public OnBoardingScreenAdapter(Context context, String[] slideHeading) {
        this.context = context;
        this.slideHeading = slideHeading;
    }

    @Override
    public int getCount() {
        return slideHeading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.on_boarding_slide_screen_layout,container,false);

        ImageView slideImageView = view.findViewById(R.id.imageView);
        TextView slideTextView = view.findViewById(R.id.textView);

        slideImageView.setImageResource(slideImages[position]);
        slideTextView.setText(slideHeading[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
