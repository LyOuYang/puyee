package com.example.puyee;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView camera = findViewById(R.id.cameraIcon);
        camera.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, ImageActivity.class);
            intent.putExtra("source", "1");
            startActivity(intent);
        });

        ImageView image = findViewById(R.id.imageIcon);
        image.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, ImageActivity.class);
            intent.putExtra("source", "2");
            startActivity(intent);
        });
    }
}
