package com.purpuligo.pcweb.Model.Adapter;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.purpuligo.pcweb.Model.Pojo.ProductSizeDetails;
import com.purpuligo.pcweb.R;

import java.util.ArrayList;

public class DeliveryOptionAdapter extends RecyclerView.Adapter<DeliveryOptionAdapter.DeliveryOptionViewHolder> {

    private String TAG = "DeliveryOptionAdapter";
    private Context context;
    private ArrayList<ProductSizeDetails> deliveryOption;
    private ArrayList<String> product_option_value_id;
    private String product_option_id;
    //private RadioButton deliveryOptionCheckbox = null;
    private int lastSelectedPosition = -1;
    private String product_category;
    private int last_selected_position = -1;

    public DeliveryOptionAdapter(Context context, ArrayList<ProductSizeDetails> deliveryOption) {
        this.context = context;
        this.deliveryOption = deliveryOption;
        setHasStableIds(true);

        product_option_value_id = new ArrayList<>();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public DeliveryOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.delivery_option_layout,parent,false);

        return new DeliveryOptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DeliveryOptionViewHolder holder, final int position) {
      try {
          holder.deliveryOptionCheckbox.setText(deliveryOption.get(position).getProduct_option_name());

          holder.deliveryOptionCheckbox.setChecked(lastSelectedPosition == position);

      }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return deliveryOption.size();
    }

    public class DeliveryOptionViewHolder extends RecyclerView.ViewHolder {

        //public RadioGroup deliveryOptionRadioGroup;
        public RadioButton deliveryOptionCheckbox;

        public DeliveryOptionViewHolder(@NonNull View itemView) {
            super(itemView);
            deliveryOptionCheckbox = itemView.findViewById(R.id.deliveryOptionCheckbox);
            deliveryOptionCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    if (product_option_value_id.size()>0){
                        product_option_value_id.set(0,deliveryOption.get(getAdapterPosition()).getProduct_option_value_id());
                    }else {
                        product_option_value_id.add(0,deliveryOption.get(getAdapterPosition()).getProduct_option_value_id());
                    }
                    product_option_id = deliveryOption.get(getAdapterPosition()).getProduct_option_id();
                    Log.d(TAG, "onClick: "+product_option_value_id+"/"+product_option_id);
                    notifyDataSetChanged();
                }
            });
            this.setIsRecyclable(false);
        }
    }

    public ArrayList<String> getProductOptionIdValue(){
        return product_option_value_id;
    }

    public String getProductOptionId(){
        return product_option_id;
    }
}
