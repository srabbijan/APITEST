package com.android.ApiTest.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ApiTest.CommonFolder.Constant;
import com.android.ApiTest.Services.Model.Data_Model;
import com.android.ApiTest.R;
import com.android.ApiTest.Screen.DetailsActivity;
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

    @SuppressLint("NotifyDataSetChanged")
    public void setUpdateData(List<Data_Model> models){
        this.models = models;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_data_list_card,parent,false);
        return new ItemHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Data_Model model = models.get(position);
        final ItemHolder itemHolder = (ItemHolder) holder;
        Glide.with(context).load(model.getPoster()).centerCrop()
                .placeholder(R.drawable.ic_error)
                .error(R.drawable.ic_error)
                .into(itemHolder.image_id);
        itemHolder.title.setText(model.getTitle());
        itemHolder.release_date.setText(model.getYear());

        itemHolder.rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.id = model.getImdbID();
                Intent i = new Intent(context, DetailsActivity.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (models!=null){
            return models.size();
        }
        return 0;
    }
}
