package com.lovesosoi.serial_android.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.lovesosoi.serial_android.DevicePort;

public class MConfig {

    @SuppressLint("StaticFieldLeak")
    private static Application sApp;

    public static void init(final Application app) {
        if (sApp != null) return;
        sApp = app;
    }

    public static Application getApp() {
        if (sApp != null) return sApp;
        return null;
    }


}
