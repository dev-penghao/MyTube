package com.penghao.mytube;

import java.io.File;

public interface UploadFile {
    public void uploadFile(File file,String host,UploadCallBack callBack);
}

abstract class UploadCallBack{
    abstract void onSuccess();
    abstract void onFaild();
}
