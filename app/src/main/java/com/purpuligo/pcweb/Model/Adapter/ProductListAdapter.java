package com.purpuligo.pcweb.Model.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.pcweb.Activity.ProductDescriptionActivity;
import com.purpuligo.pcweb.Global.NetworkState;
import com.purpuligo.pcweb.Model.Pojo.ProductListDetails;
import com.purpuligo.pcweb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    private String TAG = "ProductListAdapter";
    private Context context;
    private ArrayList<ProductListDetails> productListArray;
    private ArrayList<ProductListDetails> searchProductListArray;
    private NetworkState networkState;
    private String category_id,parent_position,prev_activity,searchQuery;

    public ProductListAdapter(Context context, ArrayList<ProductListDetails> productListArray, String category_id, String parent_position,
                              String prev_activity, String searchQuery) {
        this.context = context;
        this.productListArray = productListArray;
        this.category_id = category_id;
        this.parent_position = parent_position;
        this.prev_activity = prev_activity;
        this.searchQuery = searchQuery;

        this.searchProductListArray = new ArrayList<>();
        this.searchProductListArray.addAll(productListArray);
        networkState = new NetworkState();
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_list_demo,parent,false);

        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, final int position) {
        try{
            Log.d(TAG, "onBindViewHolder: "+productListArray.get(position).getProduct_image());
            holder.productName.setText(productListArray.get(position).getProduct_name().replace("&amp;",""));
            holder.productPrice.setText("Starting from :Rs."+String.format("%.2f",Double.parseDouble(productListArray.get(position).getProduct_price()
                    .replace("Rs.","").replace(".00",""))));
            Picasso.get().load(productListArray.get(position).getProduct_image()).into(holder.productImage);
            holder.productListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (networkState.isNetworkAvailable(context)){
                        Intent intent = new Intent(context,ProductDescriptionActivity.class);
                        intent.putExtra("product_id",productListArray.get(position).getProduct_id());
                        intent.putExtra("category_id",category_id);
                        intent.putExtra("parent_position",parent_position);
                        intent.putExtra("prev_activity",prev_activity);
                        intent.putExtra("searchQuery",searchQuery);
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show();}
                }
            });
            holder.addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (networkState.isNetworkAvailable(context)){
                        Intent intent = new Intent(context,ProductDescriptionActivity.class);
                        intent.putExtra("product_id",productListArray.get(position).getProduct_id());
                        intent.putExtra("category_id",category_id);
                        intent.putExtra("parent_position",parent_position);
                        intent.putExtra("prev_activity",prev_activity);
                        intent.putExtra("searchQuery",searchQuery);
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show();}
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return productListArray.size();
    }

    public static class ProductListViewHolder extends RecyclerView.ViewHolder {

        TextView productName,productPrice;
        ImageView productImage;
        CardView productListLayout;
        Button addToCart;

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            productListLayout = itemView.findViewById(R.id.productListLayout);
            addToCart = itemView.findViewById(R.id.addToCart);
        }
    }

    public void filter(String charText) {
        boolean flag =true;
        charText = charText.toLowerCase(Locale.getDefault());
        productListArray.clear();
        if (charText.length() == 0) {
            productListArray.addAll(productListArray.size(),searchProductListArray);
        }
        else {
            if (flag){
                for (ProductListDetails productList: searchProductListArray) {
                    if (productList.getProduct_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                        productListArray.add(productList);
                        flag = false;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}
