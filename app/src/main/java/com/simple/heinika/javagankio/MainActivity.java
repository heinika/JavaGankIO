package com.simple.heinika.javagankio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.simple.heinika.javagankio.entity.BaseEntity;
import com.simple.heinika.javagankio.entity.GirlBean;
import com.simple.heinika.javagankio.http.GankIOService;
import com.simple.heinika.javagankio.http.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = findViewById(R.id.textView);
        GankIOService gankIOService = ServiceGenerator.getInstance().getGankIOService();
        Call<BaseEntity<GirlBean>> girlsCall = gankIOService.getGirls(10,1);
        girlsCall.enqueue(new Callback<BaseEntity<GirlBean>>() {
            @Override
            public void onResponse(Call<BaseEntity<GirlBean>> call, Response<BaseEntity<GirlBean>> response) {
                if(response.body()!=null){
                    textView.setText(response.body().getResults().get(0).getUrl());
                }
            }

            @Override
            public void onFailure(Call<BaseEntity<GirlBean>> call, Throwable t) {

            }
        });
    }
}
