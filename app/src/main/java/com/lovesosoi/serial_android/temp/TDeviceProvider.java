package com.lovesosoi.serial_android.temp;

import android.util.Log;

import com.lovesosoi.serial_android.base.MSerialPort;
import com.lovesosoi.serial_android.bean.SerialConfig;
import com.lovesosoi.serial_android.constants.DeviceConstant;
import com.lovesosoi.serial_android.listener.IDeviceControl;
import com.lovesosoi.serial_android.util.MConfig;
import com.lovesosoi.serial_android.util.SerialUtils;

public class TDeviceProvider extends MSerialPort implements IDeviceControl {
    public static final String TAG = "DevicePort";

    public static TDeviceProvider getInstance() {
        if (instance == null) {
            synchronized (TDeviceProvider.class) {
                if (instance == null) {
                    instance = new TDeviceProvider();
                }
            }
        }
        return instance;
    }

    private static volatile TDeviceProvider instance;

    public TDeviceProvider() {
        super(new SerialConfig(MConfig.getApp(), DeviceConstant.DEVICE_CONFIG_NAME), status -> {
            if (status) {
                Log.e(TAG, DeviceConstant.DEVICE_CONFIG_NAME + "初始化失败");
            } else {
                Log.e(TAG, DeviceConstant.DEVICE_CONFIG_NAME + "初始化成功");
            }

        });

    }


    @Override
    protected void recvData(byte[] buffer, int size) {
        super.recvData(buffer, size);
        if (buffer.length < 4) return;
        byte[] recBuf = copyArray(buffer, size);
        int cmdtype = recBuf[3];
        String bufString = SerialUtils.bytesToHex(recBuf);

    }

    @Override
    public void openLight() {
        Log.d(TAG, "openLight() called");
        sendToSerial(DeviceConstant.OPEN_LIGHT_CMD);

    }

    @Override
    public void closeLight() {
        Log.d(TAG, "closeLight() called");
        sendCmd(DeviceConstant.CLOSEDOOR_CMD);
    }
}