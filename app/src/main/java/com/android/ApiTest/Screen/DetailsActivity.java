package com.android.ApiTest.Screen;

import static com.android.ApiTest.CommonFolder.URL.i;
import static com.android.ApiTest.CommonFolder.URL.url;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ApiTest.Adapter.Data_Adapter;
import com.android.ApiTest.Adapter.Rating_Adapter;
import com.android.ApiTest.CommonFolder.CommonHelper;
import com.android.ApiTest.CommonFolder.Constant;
import com.android.ApiTest.CommonFolder.LodingProgress;
import com.android.ApiTest.Network.ApiServices;
import com.android.ApiTest.Network.DataManger;
import com.android.ApiTest.R;
import com.android.ApiTest.Services.Model.Data_Model;
import com.android.ApiTest.Services.Model.RatingModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.mike5v.viewmoretextview.ViewMoreTextView;

public class DetailsActivity extends AppCompatActivity {
    private DataManger dataManger;
    Activity activity;
    private ImageView posterImage;
    private TextView title_text,genresTv,vote_averageTv,
            release_dateTv, runtimeTv,ActorsTv,DirectorTv,
            WriterTv,AwardsTv,CountryTv,LanguageTv;
    ViewMoreTextView overviewTv;
    private LinearLayout infoDesign;
    private RecyclerView RatingList;
    List<RatingModel> ratingModel = new ArrayList<>();
    private Rating_Adapter rating_adapter;
    private GridLayoutManager gridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        activity = this;

        Toolbar toolbar = findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());

        if (!Objects.equals(Constant.id, ""))
        hit_for_data(url+i+ Constant.id);

        posterImage = findViewById(R.id.posterImage);
        title_text = findViewById(R.id.title_text);
        genresTv = findViewById(R.id.genresTv);
        vote_averageTv = findViewById(R.id.vote_averageTv);
        release_dateTv = findViewById(R.id.release_dateTv);
        runtimeTv = findViewById(R.id.runtimeTv);
        overviewTv = findViewById(R.id.overviewTv);
        ActorsTv = findViewById(R.id.ActorsTv);
        infoDesign = findViewById(R.id.infoDesign);
        RatingList = findViewById(R.id.list_item);
        DirectorTv = findViewById(R.id.DirectorTv);
        WriterTv = findViewById(R.id.WriterTv);
        AwardsTv = findViewById(R.id.AwardsTv);
        CountryTv = findViewById(R.id.CountryTv);
        LanguageTv = findViewById(R.id.LanguageTv);

        rating_adapter =new Rating_Adapter(ratingModel, activity);
        gridLayoutManager = new GridLayoutManager(activity,1);
        RatingList.setLayoutManager(gridLayoutManager);
        RatingList.setAdapter(rating_adapter);

        overviewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overviewTv.toggle();
            }
        });
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    infoDesign.setVisibility(View.VISIBLE);
                    RatingList.setVisibility(View.GONE);
                }
                else if (tabLayout.getSelectedTabPosition() == 1) {
                    infoDesign.setVisibility(View.GONE);
                    RatingList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    private void hit_for_data(String link) {

        if (CommonHelper.isConnected(activity)){
            LodingProgress.showLoadingDialog(activity);
            dataManger = new DataManger(activity);
            dataManger.GETRequest(activity, new ApiServices() {
                @Override
                public void setJsonDataResponse(String response) {
                    try {
                        LodingProgress.hideLoadingDialog();
                        JSONObject object = new JSONObject(response);
                        String ResponseSucess = object.optString("Response");
                        if (ResponseSucess.equals("False")){
                            Log.d("GETAP", response);
                            String mes = object.optString("Error");
                            Toast.makeText(activity, ""+mes, Toast.LENGTH_SHORT).show();

                            return;
                        }
                        parse_data(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("GETAP", e.getMessage().toString());
                    }
                }

                @Override
                public void setVolleyError(VolleyError error) {
                    LodingProgress.hideLoadingDialog();
                    final int statusCode = error.networkResponse.statusCode;
                    Log.d("GETAPI", statusCode + "");
                    final String  body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    Log.d("GETAPI", body + "");
                    if (statusCode==401){
                        try {
                            JSONObject object = new JSONObject(body);
                            String mes = object.optString("Error");
                            Toast.makeText(activity, ""+mes, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, link);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void parse_data(String response) {
        try {
            JSONObject data = new JSONObject(response);
                String Title = data.optString("Title");
                String Year = data.optString("Year");
                String Released = data.optString("Released");
                String Runtime = data.optString("Runtime");
                String Genre = data.optString("Genre");
                String Director = data.optString("Director");
                String Writer = data.optString("Writer");
                String Actors = data.optString("Actors");
                String Plot = data.optString("Plot");
                String Language = data.optString("Language");
                String Country = data.optString("Country");
                String Awards = data.optString("Awards");
                String Poster = data.optString("Poster");
                String imdbRating = data.optString("imdbRating");
                String imdbVotes = data.optString("imdbVotes");
                String BoxOffice = data.optString("BoxOffice");
                String Production = data.optString("Production");
                String Website = data.optString("Website");
                String Response = data.optString("Response");
                String Type = data.optString("Type");

                JSONArray genresA = data.getJSONArray("Ratings");
                for (int i=0;i<genresA.length();i++){
                    JSONObject d = genresA.getJSONObject(i);
                    String Source =d.optString("Source");
                    String Value =d.optString("Value");
                    RatingModel model = new RatingModel();
                    model.setSource(Source);
                    model.setValue(Value);
                    ratingModel.add(model);
                    rating_adapter.notifyDataSetChanged();
                }

            title_text.setText(Title);
            genresTv.setText(Genre);
            vote_averageTv.setText(imdbRating);
            release_dateTv.setText(Released);
            ActorsTv.setText(Actors);
            runtimeTv.setText(Runtime);
            overviewTv.setText(Plot);
            DirectorTv.setText(Director);
            WriterTv.setText(Writer);
            AwardsTv.setText(Awards);
            CountryTv.setText(Country);
            LanguageTv.setText(Language);
            Glide.with(activity).load(Poster).centerCrop()
                    .placeholder(R.drawable.ic_error)
                    .error(R.drawable.ic_error)
                    .into(posterImage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}