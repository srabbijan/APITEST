package com.android.ApiTest.Screen;

import static com.android.ApiTest.CommonFolder.URL.page;
import static com.android.ApiTest.CommonFolder.URL.s;
import static com.android.ApiTest.CommonFolder.URL.url;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.android.ApiTest.CommonFolder.CommonHelper;
import com.android.ApiTest.Adapter.Data_Adapter;
import com.android.ApiTest.Services.Model.Data_Model;
import com.android.ApiTest.CommonFolder.LodingProgress;
import com.android.ApiTest.R;
import com.android.ApiTest.Services.ViewModel.LifeViewModel;
import com.google.android.material.textfield.TextInputLayout;

import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Activity activity;

    private List<Data_Model> models = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private Data_Adapter adapter;

    private RecyclerView list_item;
    private NestedScrollView nestedScrollView;
    int pageNo =  1;
    private String queryTest = "Drama";
    LifeViewModel viewModel;
    private TextInputLayout searchTxtInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        viewModel =  ViewModelProviders.of(this).get(LifeViewModel.class);
        nestedScrollView =findViewById(R.id.scroll_view_id);
        list_item =findViewById(R.id.list_item);
        searchTxtInput = findViewById(R.id.title_id);

        adapter =new Data_Adapter(models, activity);
        gridLayoutManager = new GridLayoutManager(activity,2);
        list_item.setLayoutManager(gridLayoutManager);
        list_item.setAdapter(adapter);

        Objects.requireNonNull(searchTxtInput.getEditText()).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH){
                    queryTest= searchTxtInput.getEditText().getText().toString();
                    pageNo=1;
                    models.clear();
                    hit_for_data(url+s+queryTest+page+pageNo);
                }
                return true;
            }
        });
        hit_for_data(url+s+queryTest+page+pageNo);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
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
        Log.d("url",s);
        if (CommonHelper.isConnected(activity)&&viewModel!=null){
            LodingProgress.showLoadingDialog(activity);

            viewModel.GetMovieList(s).observe(this, new Observer<List<Data_Model>>() {
                @Override
                public void onChanged( List<Data_Model> articles) {
                    models.addAll(articles);
                    adapter.setUpdateData(models);
                    LodingProgress.hideLoadingDialog();
                }
            });
        }
    }

    public void holder() throws InterruptedException {
        Thread.sleep(100);
        pageNo++;
        hit_for_data(url+s+queryTest+page+pageNo);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        models.clear();
    }
}