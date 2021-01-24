package com.purpuligo.pcweb.Model.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.purpuligo.pcweb.Activity.ProductListActivity;
import com.purpuligo.pcweb.Activity.SubCategoryActivity;
import com.purpuligo.pcweb.Global.Url;
import com.purpuligo.pcweb.Model.Pojo.ProductCategoriesDetails;
import com.purpuligo.pcweb.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder> {

    private String TAG = "CategoryListAdapter";
    private Context context;
    private ArrayList<ProductCategoriesDetails> categoriesList;
    private ArrayList<ProductCategoriesDetails> searchCategoriesList;
    private ProgressDialog progressDialog;
    private String parent_position,previousActivity;

    public CategoryListAdapter(Context context, ArrayList<ProductCategoriesDetails> categoriesList, String parent_position,
                               String previousActivity) {
        this.context = context;
        this.categoriesList = categoriesList;
        this.parent_position = parent_position;
        this.previousActivity = previousActivity;

        this.searchCategoriesList = new ArrayList<>();
        this.searchCategoriesList.addAll(categoriesList);
    }

    @NonNull
    @Override
    public CategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.category_list_layout,viewGroup,false);

        return new CategoryListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListViewHolder categoryListViewHolder, final int position) {
        try {
            if (!categoriesList.get(position).getCategory_image().equalsIgnoreCase("")){
                Picasso.get().load(categoriesList.get(position).getCategory_image()).into(categoryListViewHolder.rawFishImage);
                //Log.d(TAG, "onBindViewHolder: "+categoriesList.get(position).getCategory_image());
            }
            categoryListViewHolder.textView.setText(categoriesList.get(position).getCategory_name());
            categoryListViewHolder.category_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context,ProductListActivity.class);
//                    intent.putExtra("category_id",categoriesList.get(position).getCategory_id());
//                    intent.putExtra("parent_position",parent_position);
//                    context.startActivity(intent);
                    fetchSubCategoryType(categoriesList.get(position).getCategory_id(),parent_position,position);
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public static class CategoryListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rawFishImage) ImageView rawFishImage;
        @BindView(R.id.textView) TextView textView;
        @BindView(R.id.category_layout) CardView category_layout;
        public CategoryListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void filter(String charText) {
        boolean flag =true;
        charText = charText.toLowerCase(Locale.getDefault());
        categoriesList.clear();
        if (charText.length() == 0) {
            categoriesList.addAll(categoriesList.size(),searchCategoriesList);
        }
        else {
            if (flag){
                for (ProductCategoriesDetails categoryList: searchCategoriesList) {
                    if (categoryList.getCategory_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                        categoriesList.add(categoryList);
                        flag = false;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    private void fetchSubCategoryType(final String category_id, final String parent_position, final int position){
        showProgress();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/product/category/product&cat_id="+category_id;
        String JSON_URL = Url.PRODUCT_LIST_URL+category_id;
        Log.d(TAG, "fetchProductListDataFromServer: "+JSON_URL);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response.toString());
                        if ((response.length() > 0) & !(response == null)){
                            try {
                                JSONObject rootObject = new JSONObject(response.toString());
                                Iterator iterator = rootObject.keys();
                                while(iterator.hasNext()){
                                    String key = (String)iterator.next();
                                    Log.d(TAG, "onResponse: "+key);
                                    if (key.equalsIgnoreCase("category")){
                                        hideProgress();
                                        Log.d(TAG, "onResponse: "+key);
                                        Intent intent = new Intent(context,SubCategoryActivity.class);
                                        intent.putExtra("category_id",categoriesList.get(position).getCategory_id());
                                        intent.putExtra("parent_position",parent_position);
                                        intent.putExtra("previousActivity",previousActivity);
                                        context.startActivity(intent);
                                    }else if (key.equalsIgnoreCase("products")){
                                        hideProgress();
                                        Log.d(TAG, "onResponse: "+key);
                                        Intent intent = new Intent(context,ProductListActivity.class);
                                        intent.putExtra("category_id",categoriesList.get(position).getCategory_id());
                                        intent.putExtra("parent_position",parent_position);
                                        intent.putExtra("previousActivity",previousActivity);
                                        context.startActivity(intent);
                                    }
                                }
                            } catch (JSONException e) {
                                hideProgress();
                                e.printStackTrace();
                            }
                        }else {hideProgress();}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("error", "OnError: " + 12);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void showProgress() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Processing.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgress() {
        progressDialog.dismiss();
    }
}
