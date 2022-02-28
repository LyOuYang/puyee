package com.example.puyee;

import android.app.Application;

import com.huawei.hms.mlsdk.common.MLApplication;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class PuyeeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Petemoss-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        MLApplication.getInstance().setApiKey("DAEDALzlG1llht8MCziqUrojcPvUxLMeAcHKhbXUr/QEGQRnXGMC66nTAmeaLdxPBXIEjs8bYMSzpft0J99xm7CSFIDWwjMirkArqw==");
    }
}
