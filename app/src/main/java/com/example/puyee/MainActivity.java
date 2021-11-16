package com.example.puyee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.function.Consumer;

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
