package com.penghao.mytube;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
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

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements UploadFile, View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] title={"最新","最热","探索"};
    private List<Fragment> fragmentList=new ArrayList<>();
    private TextView myName, myNum;
    private NavigationView navigationView;
    private FloatingActionButton uploadButton;

    public static final String HOST_URL="http://192.168.1.104/";
    public static final String UPLOAD_FILE_URL=HOST_URL+"upload_file.php";
    public static final String GET_VIDEO_LIST_URL=HOST_URL+"get_video_list.php";

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
        fragmentList.add(new VideoListFragment("random"));
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void uploadFile(File file, String host, StringCallback callBack) {
        new Thread(() -> OkHttpUtils
                .post()
                .url(host)
                .addFile("file",file.getName(),file)
                .build()
                .execute(callBack)).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_action_button:
//                DialogProperties properties = new DialogProperties();
//                properties.selection_mode = DialogConfigs.SINGLE_MODE;
//                properties.selection_type = DialogConfigs.FILE_SELECT;
//                properties.root = new File(DialogConfigs.DEFAULT_DIR);
//                properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
//                properties.offset = new File(DialogConfigs.DEFAULT_DIR);
//                properties.extensions = null;
//                FilePickerDialog dialog = new FilePickerDialog(MainActivity.this,properties);
//                dialog.setTitle("Select a File");
//                dialog.setDialogSelectionListener(new DialogSelectionListener() {
//                    @Override
//                    public void onSelectedFilePaths(String[] files) {
//
//                        //files is the array of the paths of files selected by the Application User.
//                    }
//                });
//                dialog.show();
                Intent intent = new Intent(this, FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setMaxSelection(1)
                        .setShowVideos(true)
                        .setShowImages(false)
                        .enableVideoCapture(true)
                        .build());
                startActivityForResult(intent, 10);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){
            ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            System.out.println("得到文件："+files.get(0).getPath());
            uploadFile(new File(files.get(0).getPath()), UPLOAD_FILE_URL, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    System.out.println("上传文件错误："+e.toString());
                }

                @Override
                public void onResponse(String response, int id) {
                    System.out.println("文件上传结果："+response.toString());
                }

                @Override
                public void inProgress(float progress, long total, int id) {
                    super.inProgress(progress, total, id);
                    System.out.println("共："+total+"已上传："+progress);
                }
            });
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
