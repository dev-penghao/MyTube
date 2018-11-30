package com.penghao.mytube;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class VideoListFragment extends Fragment {

    final String debug="ZoneFragment";
    final String tag;
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
}
