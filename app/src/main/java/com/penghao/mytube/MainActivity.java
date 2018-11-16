package com.penghao.mytube;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Fragment homeFragment, dashboardFragment, noticeFragment, fragment_now;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            changePageFragment(item.getItemId());
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().add(R.id.main_framelayout,new HomeFragment())
                .commit();
    }
    /**
     * 当点击导航栏时改变fragment
     *
     * @param id
     */
    public void changePageFragment(int id) {
        switch (id) {
            case R.id.navigation_home:
                if (homeFragment == null) { //减少new fragment,避免不必要的内存消耗
                    homeFragment = new HomeFragment();
                }
                switchFragment(fragment_now, homeFragment);
                break;
            case R.id.navigation_dashboard:
                if (dashboardFragment == null) {
                    dashboardFragment = new DashboardFragment();
                }
                switchFragment(fragment_now, dashboardFragment);
                break;
            case R.id.navigation_notifications:
                if (noticeFragment == null) {
                    noticeFragment = new NoticeFragment();
                }
                switchFragment(fragment_now, noticeFragment);
                break;
        }
    }
    /**
     * 隐藏显示fragment
     *
     * @param from 需要隐藏的fragment
     * @param to   需要显示的fragment
     */
    public void switchFragment(Fragment from, Fragment to) {
        if (to == null) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!to.isAdded()) {
            if (from == null) {
                transaction.add(R.id.main_framelayout, to).show(to).commit();
            } else { // 隐藏当前的fragment，add下一个fragment到Activity中
                transaction.hide(from).add(R.id.main_framelayout, to).commitAllowingStateLoss();
            }
        } else { // 隐藏当前的fragment，显示下一个
            transaction.hide(from).show(to).commit();
        }
        fragment_now = to;
    }
}
