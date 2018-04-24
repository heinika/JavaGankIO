package com.simple.heinika.javagankio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.simple.heinika.javagankio.entity.BaseEntity;
import com.simple.heinika.javagankio.entity.GirlBean;
import com.simple.heinika.javagankio.girls.GirlsAdapter;
import com.simple.heinika.javagankio.http.GankIOService;
import com.simple.heinika.javagankio.http.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager layoutManager;
    private GirlsAdapter girlsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView_girls);
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        girlsAdapter = new GirlsAdapter(this);
        recyclerView.setAdapter(girlsAdapter);
    }

    private void initData() {
        GankIOService gankIOService = ServiceGenerator.getInstance().getGankIOService();
        Call<BaseEntity<GirlBean>> girlsCall = gankIOService.getGirls(50,1);
        girlsCall.enqueue(new Callback<BaseEntity<GirlBean>>() {
            @Override
            public void onResponse(Call<BaseEntity<GirlBean>> call, Response<BaseEntity<GirlBean>> response) {
                girlsAdapter.setGirlBeans(response.body().getResults());
            }

            @Override
            public void onFailure(Call<BaseEntity<GirlBean>> call, Throwable t) {

            }
        });
    }
}
