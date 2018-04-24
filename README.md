## RecyclerView 和 Glide 实现瀑布流加载网络图片问题

翻看了一些网上的文章,踩了很多坑.在这纪念下我逝去的时间吧.希望对各位有所帮助.

1. 设置 GapStrategy 为 GAP_HANDLING_NONE:这样可以防止图片在上下滑动的时候移动.

   ```java
   layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
   layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
   recyclerView.setLayoutManager(layoutManager);
   ```

2. 提前 设置 imageView 大小

   分两种情况:

   1. 已知图片的大小,在请求图片之前要先设置 ImageView 的大小.

   ```java
   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final ImageEntity.ListBean listBean = listBeans.get(position);
       Resources resources = mContext.getResources();
       DisplayMetrics dm = resources.getDisplayMetrics();
       int width = dm.widthPixels/3;
       int height = (int) (width*(Float.parseFloat(listBean.getHeight())/Float.parseFloat(listBean.getWidth())));
       RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
       layoutParams.height = height;
       holder.imageView.setLayoutParams(layoutParams);
       Glide.with(mContext).load(listBeans.get(position).getImg()).into(holder.imageView);
       holder.imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(mContext, ImageActivity.class);
               intent.putExtra("imageSrc",listBean.getImg());
               mContext.startActivity(intent);
           }
       });
   }
   ```

   2. 不知道图片大小.这种情况就需要在 onBindViewHolder 之前设置图片的大小.因为如果在 onBindViewHolder 中使用 Glide 异步的获取图片的大小会导致图片因为没有及时获取高度而出现空白.可以在 setList 处添加,比如这样:

      ```java
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
      ```

       在 onBindviewHolder 中:

      ```java
      @Override
      public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
          GirlBean girlBean = girlBeans.get(position);
          final ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
          layoutParams.width = DisplayUtils.getScreenWidth((Activity) mContext) / 2 - DisplayUtils.dp2px(mContext,8);
          if(girlBean.getScale()!=0){
              layoutParams.height = (int) (layoutParams.width/ girlBean.getScale());
          }
          holder.imageView.setBackgroundColor(Color.BLUE);
          GlideApp.with(mContext)
                  .load(girlBeans.get(position).getUrl())
                  .transition(new DrawableTransitionOptions().crossFade())
                  .into(holder.imageView);
      }
      ```

      ​

      这样就可以如你所愿的显示图片了.
![Screenshot_1524574284.png](https://upload-images.jianshu.io/upload_images/759172-e044ee5e1d35d302.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/620)


![javagankio.png](https://upload-images.jianshu.io/upload_images/759172-2d5dfad44f4865d8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/620)

 源码地址:
1. 已知图片大小: https://github.com/heinika/ImageTestDemo
2. 未知图片大小: https://github.com/heinika/JavaGankIO