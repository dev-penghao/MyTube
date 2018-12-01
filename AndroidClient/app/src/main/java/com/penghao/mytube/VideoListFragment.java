package com.penghao.mytube;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class VideoListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    final String debug="ZoneFragment";
    private final String tag;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private List<VideoItemData> datas=new ArrayList<>();
    private NewsAdapter newsAdapter;

    public VideoListFragment(String tag){
        this.tag = tag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(debug,"onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.video_list_layout,null);
        recyclerView=view.findViewById(R.id.recyclerView);
        refreshLayout=view.findViewById(R.id.swipeToRefresh);
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter=new NewsAdapter();
        recyclerView.setAdapter(newsAdapter);
        Log.d(debug,"onCreateView()");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(debug,"onStart()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(debug,"onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(debug,"onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(debug,"onDestroy");
    }

    @Override
    public void onRefresh() {

        new Thread(() -> OkHttpUtils
                .get()
                .url("http://192.168.1.104/get_video_list.php")
                .addParams("key",tag)
                .addParams("seek","0")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    // 该方法在UI线程中执行
                    @Override
                    public void onResponse(String response, int id) {
                        String[] items=response.split("\n");
                        for (String item:items){
//                            System.out.println(item);
                            Gson gson=new Gson();
                            datas.add(gson.fromJson(item,VideoItemData.class));
//                            System.out.println(videoItemData.id);
//                            System.out.println(videoItemData.filename);
//                            System.out.println(videoItemData.videoname);
//                            System.out.println(videoItemData.count);
//                            System.out.println(videoItemData.videoinfo);
                        }
                        new Thread(() -> {
                            for (int i=0;i<datas.size();i++) {
                                int finalI = i;
                                OkHttpUtils.get()
                                        .url("http://192.168.1.104/videos/"+datas.get(finalI).filename+".jpg")
                                        .build()
                                        .execute(new BitmapCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {

                                            }

                                            @Override
                                            public void onResponse(Bitmap response, int id) {
                                                datas.get(finalI).videoCover=response;
                                                newsAdapter.notifyItemChanged(finalI);
                                            }
                                        });
                            }
                            refreshLayout.setRefreshing(false);
                        }).start();
                    }
                })).start();
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{
            View itemView;
            TextView videoDration,videoCount,videoTitle;
            ImageView videoCover;
            ViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                videoDration=itemView.findViewById(R.id.video_dration);
                videoCount=itemView.findViewById(R.id.video_count);
                videoTitle=itemView.findViewById(R.id.video_title);
                videoCover=itemView.findViewById(R.id.video_cover);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.videoCover.setImageBitmap(datas.get(position).videoCover);
            holder.videoDration.setText("23:34");
            holder.videoTitle.setText(datas.get(position).videoname);
            holder.videoCount.setText("播放次数："+datas.get(position).count);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }
}
