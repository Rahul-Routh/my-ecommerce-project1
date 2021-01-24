package com.purpuligo.pcweb.Model.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.purpuligo.pcweb.Fragment.ProductFeaturesFragment;
import com.purpuligo.pcweb.Fragment.ProductInfoFragment;

public class ProductDetailsAdapter extends FragmentPagerAdapter {

    private int totalTabs;

    public ProductDetailsAdapter(@NonNull FragmentManager fm,int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ProductFeaturesFragment productFeaturesFragment = new ProductFeaturesFragment();
                return productFeaturesFragment;
            case 1:
                ProductInfoFragment productInfoFragment = new ProductInfoFragment();
                return productInfoFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
