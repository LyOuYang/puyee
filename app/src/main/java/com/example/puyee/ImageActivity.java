package com.example.puyee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        Intent intent = getIntent();
        Bitmap bitmap = intent.getParcelableExtra("bitmap");
        ImageView imageView = findViewById(R.id.image_view);
        imageView.setImageBitmap(bitmap);
    }
}