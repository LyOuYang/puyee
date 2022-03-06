package com.example.puyee.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.puyee.ImageActivity;

import java.io.File;
import java.io.FileNotFoundException;

public class CameraUtils {
    /**
     * 设置图片保存路径
     * @return
     */
    public static File getImageStoragePath(Context context){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"temp.jpg");
            return file;
        }
        return null;
    }

    public static void putOriginalBitmap(Uri imageUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");//设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("aspectX", 16);//宽高比
        intent.putExtra("aspectY", 9);
        intent.putExtra("outputX", 1080);
        intent.putExtra("outputY", 720);
        //设置了true的话直接返回bitmap，可能会很占内存
        intent.putExtra("return-data", false);
        //设置输出的格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //设置输出的地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    }
}
