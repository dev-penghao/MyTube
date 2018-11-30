package com.penghao.mytube;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements UploadFile, View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] title={"最新","最热","探索"};
    private List<Fragment> fragmentList=new ArrayList<>();
    private TextView myName, myNum;
    private NavigationView navigationView;
    private FloatingActionButton uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        uploadButton=findViewById(R.id.floating_action_button);
        uploadButton.setOnClickListener(this);
        navigationView=findViewById(R.id.nav_view_main);
        navigationView.setCheckedItem(R.id.nav_item1);
        myName=navigationView.getHeaderView(0).findViewById(R.id.nav_myname);
        myNum=navigationView.getHeaderView(0).findViewById(R.id.nav_mynum);
        // 直接像下面这两行这样做是不行的
//        myName=findViewById(R.id.nav_myname);
//        myNum=findViewById(R.id.nav_mynum);

        viewPager=findViewById(R.id.viewpager);
        tabLayout=findViewById(R.id.tabs);
        fragmentList.add(new VideoListFragment("latest"));
        fragmentList.add(new VideoListFragment("top"));
        fragmentList.add(new VideoListFragment("find"));
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        new Thread(() -> OkHttpUtils
                .get()
                .url("http://192.168.1.104/get_video_list.php")
                .addParams("key","latest")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response);
                    }
                })).start();
    }

    @Override
    public void uploadFile(File file, String host, UploadCallBack callBack) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_action_button:
//                uploadFile();
                break;
            default:
                break;
        }
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
