package com.purpuligo.ajeevikafarmfresh.Model.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.purpuligo.ajeevikafarmfresh.Activity.ProductListActivity;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductCategoriesDetails;
import com.purpuligo.ajeevikafarmfresh.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {

    private String TAG = "SubCategoryAdapter";
    private Context context;
    private ArrayList<ProductCategoriesDetails> categoriesList;
    private ArrayList<ProductCategoriesDetails> searchCategoriesList;
    private ProgressDialog progressDialog;
    private String parent_position,previousActivity;

    public SubCategoryAdapter(Context context, ArrayList<ProductCategoriesDetails> categoriesList, String parent_position,
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
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.category_list_layout,viewGroup,false);

        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder subCategoryViewHolder,final int position) {
        try {
            if (!categoriesList.get(position).getCategory_image().equalsIgnoreCase("")){
                Picasso.get().load(categoriesList.get(position).getCategory_image()).into(subCategoryViewHolder.rawFishImage);
                //Log.d(TAG, "onBindViewHolder: "+categoriesList.get(position).getCategory_image());
            }
            subCategoryViewHolder.textView.setText(categoriesList.get(position).getCategory_name());
            subCategoryViewHolder.category_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ProductListActivity.class);
                    intent.putExtra("category_id",categoriesList.get(position).getCategory_id());
                    intent.putExtra("parent_position",parent_position);
                    intent.putExtra("previousActivity",previousActivity);
                    context.startActivity(intent);
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rawFishImage) ImageView rawFishImage;
        @BindView(R.id.textView) TextView textView;
        @BindView(R.id.category_layout) CardView category_layout;
        public SubCategoryViewHolder(@NonNull View itemView) {
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
}
