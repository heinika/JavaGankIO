package com.simple.heinika.javagankio;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.simple.heinika.javagankio.entity.BaseEntity;
import com.simple.heinika.javagankio.entity.GirlBean;
import com.simple.heinika.javagankio.girls.GirlsAdapter;
import com.simple.heinika.javagankio.http.GankIOService;
import com.simple.heinika.javagankio.http.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private StaggeredGridLayoutManager layoutManager;
    private GirlsAdapter girlsAdapter;
    private List<GirlBean> girlBeans = new ArrayList<>();
    private int page;
    private GankIOService gankIOService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView_girls);
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        girlsAdapter = new GirlsAdapter(this);
        recyclerView.setAdapter(girlsAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    int[] lastPositions = new int[2];
                    layoutManager.findLastVisibleItemPositions(lastPositions);
                    if(lastPositions[0]>girlBeans.size()-3){
                        page++;
                        addGirlsToRecyclerView(page);
                    }
                }
            });
        }
    }

    private void initData() {
        gankIOService = ServiceGenerator.getInstance().getGankIOService();
        page = 1;
        addGirlsToRecyclerView(page);
    }

    private void addGirlsToRecyclerView(int page) {
        Call<BaseEntity<GirlBean>> girlsCall = gankIOService.getGirls(20,page);
        girlsCall.enqueue(new Callback<BaseEntity<GirlBean>>() {
            @Override
            public void onResponse(@NonNull Call<BaseEntity<GirlBean>> call, @NonNull Response<BaseEntity<GirlBean>> response) {
                if (response.body() != null) {
                    girlBeans.addAll(response.body().getResults());
                    girlsAdapter.setGirlBeans(girlBeans);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseEntity<GirlBean>> call, @NonNull Throwable t) {

            }
        });
    }
}
