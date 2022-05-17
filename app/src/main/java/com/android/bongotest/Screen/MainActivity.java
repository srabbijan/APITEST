package com.android.bongotest.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bongotest.CommonFolder.CommonHelper;
import com.android.bongotest.Adapter.Data_Adapter;
import com.android.bongotest.Model.Data_Model;
import com.android.bongotest.CommonFolder.LodingProgress;
import com.android.bongotest.R;
import com.android.bongotest.CommonFolder.URL;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Activity activity;
    String[] appNameArray = { "Bongo Test","Md. Fazle Rabbi","srabbijan@gmail.com" };
    private TextView appTitle;

    private List<Data_Model> models = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private Data_Adapter adapter;

    private RecyclerView list_item;
    private NestedScrollView nestedScrollView;
    int pageNo =  1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        appTitle = findViewById(R.id.appTitleId);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {


                appTitle.setText(appNameArray[i]);
                i++;
                if (i > appNameArray.length - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 2500);
            }
        };
        handler.postDelayed(runnable, 2500);

        nestedScrollView =findViewById(R.id.scroll_view_id);
        list_item =findViewById(R.id.list_item);

        adapter =new Data_Adapter(models, activity);
        gridLayoutManager = new GridLayoutManager(activity,3);
        list_item.setLayoutManager(gridLayoutManager);
        list_item.setAdapter(adapter);

        hit_for_data(URL.Top_rated_movie_api+pageNo);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    ///increase
                    try {
                        holder();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
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
            JSONObject object = new JSONObject(response);
            JSONArray results = object.getJSONArray("results");
            for (int i=0;i<results.length();i++){
                JSONObject data = results.getJSONObject(i);
                String id = data.optString("id");
                String original_title = data.optString("original_title");
                String poster_path = data.optString("poster_path");
                String release_date = data.optString("release_date");
                String vote_average = data.optString("vote_average");

                Data_Model info = new Data_Model();
                info.setId(id);
                info.setOriginal_title(original_title);
                info.setPoster_path(poster_path);
                info.setRelease_date(release_date);
                info.setVote_average(vote_average);
                models.add(info);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void holder() throws InterruptedException {
        Thread.sleep(100);
        pageNo++;
        hit_for_data(URL.Top_rated_movie_api+pageNo);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        models.clear();
//        familyBestTimeModelList.clear();
    }
}