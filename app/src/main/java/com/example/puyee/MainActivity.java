package com.example.puyee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

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
}