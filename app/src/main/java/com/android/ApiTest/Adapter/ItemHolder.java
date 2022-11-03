package com.android.ApiTest.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.ApiTest.R;

public class ItemHolder extends RecyclerView.ViewHolder {
    ImageView image_id;
    TextView title,release_date;
    LinearLayout rev;
    public ItemHolder(View item) {
        super(item);
        image_id = item.findViewById(R.id.image_id);
        title = item.findViewById(R.id.title);

        release_date = item.findViewById(R.id.release_date);
        rev = item.findViewById(R.id.rev);
    }
}
