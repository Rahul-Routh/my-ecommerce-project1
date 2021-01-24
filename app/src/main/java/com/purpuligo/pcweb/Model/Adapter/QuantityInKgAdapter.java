package com.purpuligo.pcweb.Model.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.purpuligo.pcweb.R;

import java.util.ArrayList;

public class QuantityInKgAdapter extends BaseAdapter {

    private String TAG = "QuantityInKgAdapter";
    private Context context;
    private ArrayList<String> itemQuantityInKgList;
    private int quantity_in_kg;

    public QuantityInKgAdapter(Context context, ArrayList<String> itemQuantityInKgList) {
        this.context = context;
        this.itemQuantityInKgList = itemQuantityInKgList;
    }

    @Override
    public int getCount() {
        return itemQuantityInKgList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemQuantityInKgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.size_list_layout,viewGroup,false);

        try {
            TextView sizeName = v.findViewById(R.id.sizeName);
            sizeName.setText(itemQuantityInKgList.get(position));
            quantity_in_kg = position;
            Log.d(TAG, "getView: "+quantity_in_kg);
        }catch (Exception e){e.printStackTrace();}

        return v;
    }

    public int quantityInKg(){
        return quantity_in_kg;
    }
}
