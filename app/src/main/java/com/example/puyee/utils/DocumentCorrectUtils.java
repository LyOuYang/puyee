package com.example.puyee.utils;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionAnalyzer;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionAnalyzerFactory;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionAnalyzerSetting;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionConstant;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionCoordinateInput;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionResult;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewDetectResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentCorrectUtils {

    private static final String TAG = DocumentCorrectUtils.class.getSimpleName();

    public Bitmap correctiveBitmap(Bitmap bitmap) {
        MLDocumentSkewCorrectionAnalyzerSetting setting = new MLDocumentSkewCorrectionAnalyzerSetting.Factory().create();
        MLDocumentSkewCorrectionAnalyzer analyzer = MLDocumentSkewCorrectionAnalyzerFactory.getInstance().getDocumentSkewCorrectionAnalyzer(setting);
        MLFrame frame = MLFrame.fromBitmap(bitmap);
        SparseArray<MLDocumentSkewDetectResult> detect = analyzer.analyseFrame(frame);
        if (detect  != null && detect.get(0).getResultCode() == MLDocumentSkewCorrectionConstant.SUCCESS) {
            // 检测成功。
            MLDocumentSkewDetectResult detectResult = detect.get(0);
            Point leftTop = detectResult.getLeftTopPosition();
            Point rightTop = detectResult.getRightTopPosition();
            Point leftBottom = detectResult.getLeftBottomPosition();
            Point rightBottom = detectResult.getRightBottomPosition();
            List<Point> coordinates = new ArrayList<>();
            coordinates.add(leftTop);
            coordinates.add(rightTop);
            coordinates.add(rightBottom);
            coordinates.add(leftBottom);
            MLDocumentSkewCorrectionCoordinateInput coordinateData = new MLDocumentSkewCorrectionCoordinateInput(coordinates);
        } else {
        // Detect exception.
        Log.e(TAG, "Detect exception！");
        DocumentCorrectUtils.this.displayFailure();
    }
        if (analyzer != null) {
            try {
                analyzer.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displaySuccess(MLDocumentSkewCorrectionResult refineResult) {
        if (this.bitmap == null) {
            this.displayFailure();
            return;
        }
        // Draw the portrait with a transparent background.
        Bitmap corrected = refineResult.getCorrected();
        if (corrected != null) {
            this.mImageView.setImageBitmap(corrected);
        } else {
            this.displayFailure();
        }
    }


    // Refine image
    private void refineImg() {
        // Call refine image interface
        Task<MLDocumentSkewCorrectionResult> correctionTask = this.correctiveBitmap().asyncDocumentSkewCorrect(this.mlFrame, this.input);
        correctionTask.addOnSuccessListener(new OnSuccessListener<MLDocumentSkewCorrectionResult>() {
            @Override
            public void onSuccess(MLDocumentSkewCorrectionResult refineResult) {
                if (refineResult != null) {
                    int resultCode = refineResult.getResultCode();
                    if (resultCode == MLDocumentSkewCorrectionConstant.SUCCESS) {
                        DocumentCorrectUtils.this.displaySuccess(refineResult);
                    } else if (resultCode == MLDocumentSkewCorrectionConstant.IMAGE_DATA_ERROR) {
                        // Parameters error.
                        Log.e(TAG, "Parameters error！");
                        DocumentCorrectUtils.this.displayFailure();
                    } else if (resultCode == MLDocumentSkewCorrectionConstant.CORRECTION_FAILED) {
                        // Correct failure.
                        Log.e(TAG, "Correct failed！");
                        DocumentCorrectUtils.this.displayFailure();
                    }
                } else {
                    // Correct exception.
                    Log.e(TAG, "Correct exception！");
                    DocumentCorrectUtils.this.displayFailure();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Processing logic for refine failure.
                DocumentCorrectUtils.this.displayFailure();
            }
        });
    }

    private void displayFailure() {
        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
    }


}
