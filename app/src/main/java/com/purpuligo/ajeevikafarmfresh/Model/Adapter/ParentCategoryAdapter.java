package com.purpuligo.ajeevikafarmfresh.Model.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purpuligo.ajeevikafarmfresh.Activity.CategoryListActivity;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ParentDetails;
import com.purpuligo.ajeevikafarmfresh.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ParentCategoryAdapter extends RecyclerView.Adapter<ParentCategoryAdapter.RawFishViewHolder> {

    private static final String TAG = "ParentCategoryAdapter";

    Context context;
    private ArrayList<ParentDetails> rawFishData;

    public ParentCategoryAdapter(Context context, ArrayList<ParentDetails> rawFishData) {
        this.context = context;
        this.rawFishData = rawFishData;
        Log.d(TAG, "ParentCategoryAdapter: "+rawFishData.toString());
        //activity = new Activity();
    }

    @NonNull
    @Override
    public RawFishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_view_layout,parent,false);

        return new RawFishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RawFishViewHolder holder, final int position) {
        try{
            holder.textView.setText(rawFishData.get(position).getParent_name());
            Picasso.get().load(rawFishData.get(position).getParent_image()).into(holder.rawFishImage);

            holder.productType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,CategoryListActivity.class);
                    intent.putExtra("parent_position",rawFishData.get(position).getParent_id());
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    Log.d(TAG, "onClick: "+rawFishData.get(position).getCategories());

//                    Intent intent = new Intent(context,ProductListActivity.class);
//                    intent.putExtra("category_id",rawFishData.get(position).getParent_id());
//                    Log.d(TAG, "onCreate: "+rawFishData.get(position).getParent_id());
//                    context.startActivity(intent);
//                    ((Activity)context).finish();

//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("categoriesPosition", position);
//                    BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialogFragment();
//                    bottomSheetDialogFragment.setArguments(bundle);
//                    bottomSheetDialogFragment.show(activity.getSupportFragmentManager(),"bottomSheet");

                }
            });

//            if (position == 0){
//                holder.rawFishImage.setImageResource(R.drawable.pic_three);
//            }
//            if (position == 1){
//                holder.rawFishImage.setImageResource(R.drawable.pic_five);
//            }
//            if (position == 2){
//                holder.rawFishImage.setImageResource(R.drawable.table_booking);
//            }
//            if (position == 3){
//                holder.rawFishImage.setImageResource(R.drawable.grocery);
//            }
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return rawFishData.size();
    }

    public static class RawFishViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        LinearLayout productType;
        ImageView rawFishImage;

        public RawFishViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            productType = itemView.findViewById(R.id.productType);
            rawFishImage = itemView.findViewById(R.id.rawFishImage);
        }
    }
}
