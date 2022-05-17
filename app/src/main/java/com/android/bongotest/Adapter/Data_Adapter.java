package com.android.bongotest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bongotest.CommonFolder.Constant;
import com.android.bongotest.Model.Data_Model;
import com.android.bongotest.R;
import com.android.bongotest.CommonFolder.URL;
import com.android.bongotest.Screen.DetailsActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Data_Adapter extends RecyclerView.Adapter {
    List<Data_Model> models = new ArrayList<>();
    Context context;

    public Data_Adapter(List<Data_Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_besttime_profile_list_card,parent,false);
        return new ItemHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Data_Model model = models.get(position);
        final ItemHolder itemHolder = (ItemHolder) holder;
        Glide.with(context).load(URL.image+model.getPoster_path()).centerCrop()
                .placeholder(R.drawable.ic_error)
                .error(R.drawable.ic_error)
                .into(itemHolder.image_id);
        itemHolder.title.setText(model.getOriginal_title());
        itemHolder.release_date.setText(model.getRelease_date().substring(0,4));
        itemHolder.vote_average.setText(model.getVote_average());
        itemHolder.rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.id = model.getId();
                Intent i = new Intent(context, DetailsActivity.class);
                context.startActivity(i);
//                Toast.makeText(context, ""+Constant.id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
