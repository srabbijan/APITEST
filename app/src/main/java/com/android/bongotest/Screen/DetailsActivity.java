package com.android.bongotest.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bongotest.CommonFolder.CommonHelper;
import com.android.bongotest.CommonFolder.Constant;
import com.android.bongotest.CommonFolder.LodingProgress;
import com.android.bongotest.CommonFolder.URL;
import com.android.bongotest.Model.Data_Model;
import com.android.bongotest.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import it.mike5v.viewmoretextview.ViewMoreTextView;

public class DetailsActivity extends AppCompatActivity {
    Activity activity;
    private ImageView posterImage;
    CircleImageView production_companiesIv;
    private TextView title_text,genresTv,vote_averageTv,release_dateTv,production_companiesTv,
    runtimeTv;
    ViewMoreTextView overviewTv;
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
//        Toast.makeText(activity, URL.Movie_details_api, Toast.LENGTH_SHORT).show();
        hit_for_data(URL.MAIN_URL+ Constant.id+URL.Last_Part2);
        posterImage = findViewById(R.id.posterImage);
        title_text = findViewById(R.id.title_text);
        genresTv = findViewById(R.id.genresTv);
        vote_averageTv = findViewById(R.id.vote_averageTv);
        release_dateTv = findViewById(R.id.release_dateTv);
        production_companiesIv = findViewById(R.id.production_companiesIv);
        production_companiesTv = findViewById(R.id.production_companiesTv);
        runtimeTv = findViewById(R.id.runtimeTv);
        overviewTv = findViewById(R.id.overviewTv);
        overviewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overviewTv.toggle();
            }
        });

    }
    private void hit_for_data(String s) {
        Log.d("fam_story",s);
        if (CommonHelper.isConnected(activity)){
            LodingProgress.showLoadingDialog(activity);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,s ,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("fam_story",response);
//                            Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                            LodingProgress.hideLoadingDialog();
                            parse_data(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error == null || error.networkResponse == null) {
                                return;
                            }
                            String body;
                            //get status code here
                            final String statusCode = String.valueOf(error.networkResponse.statusCode);
                            //get response body and parse with appropriate encoding
                            Log.d("fam_home",statusCode+"");
                            try {
                                body = new String(error.networkResponse.data,"UTF-8");
                                Log.d("fam_home",body+"");

                            } catch (UnsupportedEncodingException e) {
                                // exception
                                Toast.makeText(activity, ""+e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue queue = Volley.newRequestQueue(activity);
            queue.add(stringRequest);
            queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                @Override
                public void onRequestFinished(Request<Object> request) {
                    queue.getCache().clear();
                }
            });
        }
    }

    private void parse_data(String response) {
        try {
            JSONObject data = new JSONObject(response);


                String id = data.optString("id");
                String original_title = data.optString("original_title");
                String overview = data.optString("overview");
                String poster_path = data.optString("poster_path");
                String genres = " ";
                JSONArray genresA = data.getJSONArray("genres");
                for (int i=0;i<genresA.length();i++){
                    JSONObject d = genresA.getJSONObject(i);
                    genres+=d.optString("name");
                    genres=genres+", ";
                }
                JSONArray array = data.getJSONArray("production_companies");
                String companies_logo_path = "";
                String companies_name = "";

                for (int i =0;i<array.length();i++){
                    JSONObject object = array.optJSONObject(i);
                    companies_logo_path = object.optString("logo_path");
                    companies_name = object.optString("name");
                }
                String runtime = data.optString("runtime");
                String release_date = data.optString("release_date");
                String vote_average = data.optString("vote_average");

            title_text.setText(original_title);
            genresTv.setText(genres.substring(0,genres.length()-2));
            vote_averageTv.setText(vote_average);
            release_dateTv.setText(release_date.substring(0,4));
            production_companiesTv.setText(companies_name);
            runtimeTv.setText(getHour(Integer.parseInt(runtime)));
            overviewTv.setText(overview);
            Glide.with(activity).load(URL.image+poster_path).centerCrop()
                    .placeholder(R.drawable.ic_error)
                    .error(R.drawable.ic_error)
                    .into(posterImage);
            Glide.with(activity).load(URL.image+companies_logo_path).centerCrop()
                    .placeholder(R.drawable.ic_error)
                    .error(R.drawable.ic_error)
                    .into(production_companiesIv);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getHour(int runtime) {
        int hour = runtime/60;
        int min  = runtime%60;
        return hour+" h "+min+" min";
    }
}