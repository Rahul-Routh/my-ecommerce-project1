package com.purpuligo.ajeevikafarmfresh.Model.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductSizeDetails;
import com.purpuligo.ajeevikafarmfresh.R;

import java.util.ArrayList;

public class SizeListAdapter extends BaseAdapter {

    private static final String TAG = "SizeListAdapter";
    private Context context;
    private ArrayList<ProductSizeDetails> productSizeList;
    private String product_option_id;
    //private String product_category;
    private String size_product_option_value_id;

    public SizeListAdapter(Context context, ArrayList<ProductSizeDetails> productSizeList) {
        this.context = context;
        this.productSizeList = productSizeList;
    }

    @Override
    public int getCount() {
        return productSizeList.size();
    }

    @Override
    public Object getItem(int position) {
        return productSizeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.size_list_layout,viewGroup,false);

        try{
            TextView sizeName = v.findViewById(R.id.sizeName);
            sizeName.setText(productSizeList.get(position).getProduct_option_name().replace("amp;",""));
            size_product_option_value_id = productSizeList.get(position).getProduct_option_value_id();
            product_option_id = productSizeList.get(position).getProduct_option_id();
            //product_category = productSizeList.get(position).getProduct_category();
        }catch (Exception e){e.printStackTrace();}

        return v;
    }

    public String getProductOptionIdValue(){
        return size_product_option_value_id;
    }

    public String getProductOptionId(){
        return product_option_id;
    }

//    public String getProductCategory(){
//        return product_category;
//    }
}