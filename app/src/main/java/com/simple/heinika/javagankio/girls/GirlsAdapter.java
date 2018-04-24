package com.simple.heinika.javagankio.girls;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.simple.heinika.javagankio.R;
import com.simple.heinika.javagankio.entity.GirlBean;
import com.simple.heinika.javagankio.utils.DisplayUtils;

import java.util.List;

public class GirlsAdapter extends RecyclerView.Adapter<GirlsAdapter.ViewHolder> {
    private Context mContext;

    public void setGirlBeans(List<GirlBean> girlBeans) {
        this.girlBeans = girlBeans;
        setImageScale();
    }

    private void setImageScale() {
        for (final GirlBean girlBean : girlBeans) {
            if(girlBean.getScale() == 0){
                Glide.with(mContext).load(girlBean.getUrl()).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        float scale = resource.getIntrinsicWidth() / (float) resource.getIntrinsicHeight();
                        girlBean.setScale(scale);
                        notifyDataSetChanged();
                    }
                });
            }else {
                notifyDataSetChanged();
            }
        }
    }

    private List<GirlBean> girlBeans;

    public GirlsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_girl, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        GirlBean girlBean = girlBeans.get(position);
        final ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.width = DisplayUtils.getScreenWidth((Activity) mContext) / 2 - DisplayUtils.dp2px(mContext,8);
        if(girlBean.getScale()!=0){
            layoutParams.height = (int) (layoutParams.width/ girlBean.getScale());
        }
        holder.imageView.setBackgroundColor(Color.BLUE);
        Glide.with(mContext)
                .load(girlBeans.get(position).getUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return girlBeans == null ? 0 : girlBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItemGirl);
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }
}
