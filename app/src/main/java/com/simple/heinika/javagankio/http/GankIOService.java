package com.simple.heinika.javagankio.http;

import com.simple.heinika.javagankio.entity.BaseEntity;
import com.simple.heinika.javagankio.entity.GirlBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GankIOService {
    @GET("data/福利/{num}/{page}")
    Call<BaseEntity<GirlBean>> getGirls(@Path("num")int nym,@Path("page")int page);
}
