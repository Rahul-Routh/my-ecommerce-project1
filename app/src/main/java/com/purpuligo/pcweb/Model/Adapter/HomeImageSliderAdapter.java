package com.purpuligo.pcweb.Model.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.purpuligo.pcweb.Model.Pojo.ImageSliderPojo;
import com.purpuligo.pcweb.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HomeImageSliderAdapter extends PagerAdapter {
    private static final String TAG = "HomeImageSliderAdapter";
    private Context context;
    private List<ImageSliderPojo> imageSliderList;
    //private Integer[] images = {R.drawable.slider10,R.drawable.slide7,R.drawable.slide8};
    //private List<Integer> color;

    public HomeImageSliderAdapter(Context context, List<ImageSliderPojo> imageSliderList) {
        this.context = context;
        this.imageSliderList = imageSliderList;
    }

    @Override
    public int getCount() {
        return imageSliderList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.home_image_slider, null);

        //ImageView imageView = view.findViewById(R.id.imageView);
        //imageView.setImageResource(images[position]);
        try{
            ImageView linearLayout = view.findViewById(R.id.linearLayout);
            //ImageView linearLayout = view.findViewById(R.id.linearLayout);
            //linearLayout.setBackgroundResource(images[position]);
            //linearLayout.setBackgroundResource();
            Picasso.get().load(imageSliderList.get(position).getSlider_url()).into(linearLayout);
            Log.d(TAG, "instantiateItem: "+imageSliderList.get(position).getSlider_url());
        }catch (Exception e){e.printStackTrace();}

//        CardView cardView = view.findViewById(R.id.cardView);
//        cardView.setBackgroundResource(images[position]);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position,@NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
