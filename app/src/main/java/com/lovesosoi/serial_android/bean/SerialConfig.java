package com.lovesosoi.serial_android.bean;

import android.content.Context;

import java.io.Serializable;

public class SerialConfig implements Serializable {
    private Context mContext;
    private String configName;

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public SerialConfig(Context mContext, String configName) {

        this.mContext = mContext;
        this.configName = configName;
    }
}
