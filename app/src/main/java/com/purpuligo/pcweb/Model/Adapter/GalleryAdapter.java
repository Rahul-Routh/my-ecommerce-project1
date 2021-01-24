package com.purpuligo.pcweb.Model.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.purpuligo.pcweb.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    Context context;
    private Integer[] items = {R.drawable.background,R.drawable.background_one,R.drawable.background_two,R.drawable.background_three};

    public GalleryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.gallery_item_layout,parent,false);

        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        try{
            holder.linearLayout.setBackgroundResource(items[position]);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
