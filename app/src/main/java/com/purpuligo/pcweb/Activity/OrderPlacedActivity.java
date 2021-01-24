package com.purpuligo.pcweb.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.pcweb.Global.NetworkState;
import com.purpuligo.pcweb.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderPlacedActivity extends AppCompatActivity {

    @BindView(R.id.continue_shopping) Button continue_shopping;
    @BindView(R.id.order_id_tv) TextView order_id_tv;
    private NetworkState networkState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        ButterKnife.bind(this);
        networkState = new NetworkState();

        try{ Intent intent = getIntent();
            order_id_tv.setText(intent.getStringExtra("order_id"));
        }catch (Exception e){e.printStackTrace();}

        continue_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkState.isNetworkAvailable(OrderPlacedActivity.this)){
                    Intent intent = new Intent(OrderPlacedActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    OrderPlacedActivity.this.finish();
                }else {
                    Toast.makeText(OrderPlacedActivity.this, "Please connect to internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
