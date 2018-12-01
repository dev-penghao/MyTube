package com.penghao.mytube;

import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

public interface UploadFile {
    public void uploadFile(File file, String host, StringCallback callback);
}