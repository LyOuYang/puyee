package com.example.puyee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.puyee.bean.recognize.RecognizeRsp;
import com.example.puyee.utils.CameraUtils;
import com.example.puyee.utils.ConvertUtils;
import com.example.puyee.utils.DocumentCorrectUtils;
import com.example.puyee.utils.NetworkUtils;
import com.example.puyee.view.DocumentCorrectImageView;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionAnalyzer;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewDetectResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends AppCompatActivity {
    private static final int STORAGE_REQ_CODE = 10;
    PhotoView imageView;
    Button confirm;
    Button cancel;
    RecognizeRsp result;
    ProgressBar progress;
    DocumentCorrectImageView documetScanView;
    Bitmap bitmap;
    Uri imageUri;
    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == 1) {
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "没有找到音符", Toast.LENGTH_SHORT).show();
                    return;
                }
                progress.setVisibility(View.GONE);
                confirm.setClickable(true);
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                // 抗锯齿
                paint.setAntiAlias(true);
                // 防抖动
                paint.setDither(true);
                List<String> classes = result.getDetection_classes();
                List<Double> scores = result.getDetection_scores();
                List<List<Double>> boxes = result.getDetection_boxes();
                for(int i = 0; i < boxes.size(); i++) {
                    if(scores.get(i) > 0.6) {
                        List<Double> box = boxes.get(i);
                        Double y1 = box.get(0);
                        Double x1 = box.get(1);
                        Double y2 = box.get(2);
                        Double x2 = box.get(3);
//                        Rect bounds = new Rect();
//                        paint.getTextBounds(classes.get(i), 0, classes.get(i).length(), bounds);
//                        int width = bounds.width();
                        setTextSizeForWidth(paint, (float) (x2-x1), classes.get(i));
                        paint.setColor(Color.parseColor("#ff0000"));
                        int num = ConvertUtils.convertToNumber(classes.get(i));
                        canvas.drawText(String.valueOf(num), (float) (x1 - 0), (float) (y1 - 0), paint);
                    }
                }
                imageView.setImageBitmap(bitmap);
            }
        } };

    private static void setTextSizeForWidth(Paint paint, float desiredWidth, String text) {

        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * desiredWidth / bounds.width();

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }
    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            byte[] bytes = NetworkUtils.bitmapToByte(bitmap, Bitmap.CompressFormat.PNG);
            result = NetworkUtils.getPuyeeRecognize(bytes);
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        imageView = findViewById(R.id.image_view);
        progress = findViewById(R.id.progressBar);
        cancel = findViewById(R.id.cancel);
        Button adjust = findViewById(R.id.adjust_confirm);
        documetScanView = findViewById(R.id.iv_documetscan);
        cancel.setOnClickListener((v) -> {
            finish();
        });
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(imageView);
        pAttacher.update();
        Intent intent = getIntent();
        String source = intent.getStringExtra("source");
        isGetPermission();
        if (source.equals("1")) {
//            Intent image = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(image, 1);
            takePhotos();
        } else if (source.equals("2")) {
            Intent in = new Intent(Intent.ACTION_PICK);
            //指定获取的是图片
            in.setType("image/*");
            startActivityForResult(in, 2);
        }

        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener((v) -> {
            Thread thread = new Thread(r1);
            thread.start();
            confirm.setClickable(false);
            progress.setVisibility(View.VISIBLE);
        });

        adjust.setOnClickListener(v -> {
            Bitmap correctiveBitmap = DocumentCorrectUtils.correctiveBitmap(documetScanView, this.bitmap);
            imageView.setImageBitmap(correctiveBitmap);
            documetScanView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        });
    }

    /**
     * 拍照并存储到相册
     *
     */
    public void takePhotos() {
        //指定照片存储路径
        File cameraPhoto = CameraUtils.getImageStoragePath(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = FileProvider.getUriForFile(ImageActivity.this, "puyee.fileprovider", cameraPhoto);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,3);
    }

    private boolean isGetPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        // 否----》弹框让用户给予权限
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(ImageActivity.this, permission, STORAGE_REQ_CODE);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_REQ_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "欢迎下次光临", Toast.LENGTH_SHORT).show();
    }

    // 在某种满足的情况下被
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (data == null) {
//            onDestroy();
//            finish();
//            return;
//        }
        bitmap = null;
        if (requestCode == 1) {
            bitmap = data.getParcelableExtra("data");
        }

        if (requestCode == 2) {
            Uri uris = data.getData();
            bitmap = getBitmapFromUri(uris);
        }

        if (requestCode == 3) {
            CameraUtils.putOriginalBitmap(imageUri);
            bitmap = getBitmapFromUri(imageUri);
        }

        if (bitmap == null) {
            return;
        }
        reloadAndDetectImage();
    }

    private void reloadAndDetectImage() {
        if (bitmap == null) {
            return;
        }

        MLFrame frame = MLFrame.fromBitmap(bitmap);
        MLDocumentSkewCorrectionAnalyzer analyzer = DocumentCorrectUtils.createAnalyzer();
        Task<MLDocumentSkewDetectResult> task = analyzer.asyncDocumentSkewDetect(frame);
        task.addOnSuccessListener(new OnSuccessListener<MLDocumentSkewDetectResult>() {
            public void onSuccess(MLDocumentSkewDetectResult result) {
                if (result.getResultCode() != 0) {
                    documetScanView.setImageBitmap(bitmap);
                    Point[] _points = getDefaultPoint();
                    documetScanView.setPoints(_points);
                    Toast.makeText(ImageActivity.this, "The picture does not meet the requirements.", Toast.LENGTH_SHORT).show();
                } else {
                    // Recognition success.
                    Point leftTop = result.getLeftTopPosition();
                    Point rightTop = result.getRightTopPosition();
                    Point leftBottom = result.getLeftBottomPosition();
                    Point rightBottom = result.getRightBottomPosition();

                    Point[] _points = new Point[4];
                    _points[0] = leftTop;
                    _points[1] = rightTop;
                    _points[2] = rightBottom;
                    _points[3] = leftBottom;
                    imageView.setVisibility(View.GONE);
                    documetScanView.setImageBitmap(bitmap);
                    documetScanView.setPoints(_points);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Point[] getDefaultPoint() {
        Point[] _points = new Point[4];
        _points[0] = new Point(0,0);
        _points[1] = new Point(bitmap.getWidth(), 0);
        _points[2] = new Point(bitmap.getWidth(), bitmap.getHeight());
        _points[3] = new Point(0, bitmap.getHeight());
        return _points;
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