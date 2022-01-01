package com.example.puyee.utils;

public class ConvertUtils {
    public static int convertToNumber(String s) {
        if(s.equals("C")) {
            return 1;
        }
        if(s.equals("D")) {
            return 2;
        }
        if(s.equals("E")) {
            return 3;
        }
        if(s.equals("F")) {
            return 4;
        }
        if(s.equals("G")) {
            return 5;
        }
        if(s.equals("A")) {
            return 6;
        }
        if(s.equals("B")) {
            return 7;
        }
        return -1;
    }
}
