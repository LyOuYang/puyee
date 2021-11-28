package com.example.puyee.bean.recognize;

import java.util.List;

public class RecognizeRsp {
    private List<String> detection_classes;
    private List<List<Double>> detection_boxes;
    private List<Double> detection_scores;

    public List<String> getDetection_classes() {
        return detection_classes;
    }

    public void setDetection_classes(List<String> detection_classes) {
        this.detection_classes = detection_classes;
    }

    public List<List<Double>> getDetection_boxes() {
        return detection_boxes;
    }

    public void setDetection_boxes(List<List<Double>> detection_boxes) {
        this.detection_boxes = detection_boxes;
    }

    public List<Double> getDetection_scores() {
        return detection_scores;
    }

    public void setDetection_scores(List<Double> detection_scores) {
        this.detection_scores = detection_scores;
    }
}
