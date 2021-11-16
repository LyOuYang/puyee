package com.example.puyee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class ImageActivity extends BaseActivity {
    private static final int STORAGE_REQ_CODE = 10;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        imageView = findViewById(R.id.image_view);
        Intent intent = getIntent();
        String source = intent.getStringExtra("source");
        if (source.equals("1")) {
            Intent image = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(image, 1);
        } else if (source.equals("2")) {
            isGetPermission();
            Intent in = new  Intent(Intent.ACTION_PICK);
            //指定获取的是图片
            in.setType("image/*");
            startActivityForResult(in, 2);
        }
    }

    private boolean isGetPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        // 否----》弹框让用户给予权限
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(ImageActivity.this, permission, STORAGE_REQ_CODE);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_REQ_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "权限获取成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "权限获取失败", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 11) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BIND_VOICE_INTERACTION) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "权限获取成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "权限获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean getVideoPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BIND_VOICE_INTERACTION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        // 否----》弹框让用户给予权限
        String[] permission = {Manifest.permission.BIND_VOICE_INTERACTION};
        ActivityCompat.requestPermissions(ImageActivity.this, permission, 11);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "欢迎下次光临", Toast.LENGTH_SHORT).show();
    }

    // 在某种满足的情况下被
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            onDestroy();
            finish();
            return;
        }
        Bitmap bitmap = null;
        if (requestCode == 1) {
            bitmap = data.getParcelableExtra("data");
        } else {
            Uri uris = data.getData();
            bitmap = getBitmapFromUri(uris);
        }

        if (bitmap == null) {
            return;
        }

        imageView.setImageBitmap(bitmap);
    }

    private Bitmap getBitmapFromUri(Uri uris) {
        Bitmap bitmap = null;
        try {
            bitmap =  BitmapFactory.decodeStream(getContentResolver().openInputStream(uris));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "FileNotFoundException", Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }
}