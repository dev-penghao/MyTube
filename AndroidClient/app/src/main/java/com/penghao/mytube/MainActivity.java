package com.penghao.mytube;

import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] title={"最新","最热","探索"};
    private List<Fragment> fragmentList=new ArrayList<>();
    private TextView myName, myNum;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        navigationView=findViewById(R.id.nav_view_main);
        navigationView.setCheckedItem(R.id.nav_item1);
        myName=navigationView.getHeaderView(0).findViewById(R.id.nav_myname);
        myNum=navigationView.getHeaderView(0).findViewById(R.id.nav_mynum);
        // 直接像下面这两行这样做是不行的
//        myName=findViewById(R.id.nav_myname);
//        myNum=findViewById(R.id.nav_mynum);

        viewPager=findViewById(R.id.viewpager);
        tabLayout=findViewById(R.id.tabs);
        fragmentList.add(new VideoListFragment("lastest"));
        fragmentList.add(new VideoListFragment("top"));
        fragmentList.add(new VideoListFragment("find"));
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
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
