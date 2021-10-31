package com.example.puyee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView camera = findViewById(R.id.cameraIcon);
        camera.setOnClickListener((v) -> {
            Intent image = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(image, 1);
        });

        ImageView image = findViewById(R.id.imageIcon);
        image.setOnClickListener((v) -> {
            Intent intent = new Intent(Intent.ACTION_PICK); //指定获取的是图片
            intent.setType("image/*"); startActivityForResult(intent, REQUEST_IMAGE_OPEN);
        });

    }

    // 在某种满足的情况下被
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Bitmap pic = data.getParcelableExtra("data");
        Intent intent = new Intent(MainActivity.this, ImageActivity.class);
        intent.putExtra("bitmap", pic);
        startActivity(intent);
        // 调转到image对应的activity
        // 参数带过去
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //将选择的图片显示
        if(requestCode == REQUEST_IMAGE_OPEN && resultCode == Activity.RESULT_OK && data != null){
            Uri uris;
            uris = data.getData(); Bitmap bitmap = null; //Uri转化为Bitmap
            try {
                bitmap = getBitmapFromUri(uris);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
        }
    }

}
