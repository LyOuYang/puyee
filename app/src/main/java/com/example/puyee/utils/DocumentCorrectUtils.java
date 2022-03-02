package com.example.puyee.utils;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.SparseArray;

import com.example.puyee.view.DocumentCorrectImageView;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionAnalyzer;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionAnalyzerFactory;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionAnalyzerSetting;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionConstant;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionCoordinateInput;
import com.huawei.hms.mlsdk.dsc.MLDocumentSkewCorrectionResult;
import java.util.ArrayList;
import java.util.List;

public class DocumentCorrectUtils {
    public static Bitmap correctiveBitmap(DocumentCorrectImageView documetScanView, Bitmap bitmap) {
        MLFrame frame = MLFrame.fromBitmap(bitmap);
        List<Point> points = new ArrayList<>();
        Point[] cropPoints = documetScanView.getCropPoints();
        if (cropPoints != null) {
            points.add(cropPoints[0]);
            points.add(cropPoints[1]);
            points.add(cropPoints[2]);
            points.add(cropPoints[3]);
        }
        MLDocumentSkewCorrectionCoordinateInput coordinateData = new MLDocumentSkewCorrectionCoordinateInput(points);
        return getDetectdetectResult(coordinateData, frame);
    }

    public static MLDocumentSkewCorrectionAnalyzer createAnalyzer() {
        MLDocumentSkewCorrectionAnalyzerSetting setting = new MLDocumentSkewCorrectionAnalyzerSetting
                .Factory()
                .create();
        return MLDocumentSkewCorrectionAnalyzerFactory.getInstance().getDocumentSkewCorrectionAnalyzer(setting);
    }

    private static Bitmap getDetectdetectResult(MLDocumentSkewCorrectionCoordinateInput coordinateData, MLFrame frame) {
        MLDocumentSkewCorrectionAnalyzer analyzer = createAnalyzer();
        // syncDocumentSkewCorrect同步调用。
        SparseArray<MLDocumentSkewCorrectionResult> correct= analyzer.syncDocumentSkewCorrect(frame,  coordinateData);
        if (correct != null && correct.get(0).getResultCode() == MLDocumentSkewCorrectionConstant.SUCCESS) {
            // 校正成功。
            return correct.get(0).getCorrected();
        }
        return null;
    }
}
