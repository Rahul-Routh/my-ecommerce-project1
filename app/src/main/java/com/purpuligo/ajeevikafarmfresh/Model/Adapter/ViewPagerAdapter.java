package com.purpuligo.ajeevikafarmfresh.Model.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.purpuligo.ajeevikafarmfresh.R;

import java.io.IOException;
import java.net.URL;

public class ViewPagerAdapter extends PagerAdapter {

    private static final String TAG = "ViewPagerAdapter";
    private Context context;
    private String descriptionImage;
    //private Integer[] images = {R.drawable.restaurant_one,R.drawable.restaurant_two,R.drawable.raw_fish_one,R.drawable.raw_fish_two};

    public ViewPagerAdapter(Context context, String descriptionImage) {
        this.context = context;
        this.descriptionImage = descriptionImage;
    }

    @Override
    public int getCount() {
        return descriptionImage.length();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.description_image_slider,null);

        ImageView imageView = view.findViewById(R.id.imageView);
        //Picasso.get().load(descriptionImage.get(position).getImageUrl()).into(imageView);
        try {
            URL url = new URL(descriptionImage);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(image);
        } catch(IOException e) {
            Log.d(TAG, "instantiateItem: "+e);
        }


        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
