package com.lovesosoi.serial_android.temp;

import android.util.Log;

import com.lovesosoi.serial_android.base.MSerialPort;
import com.lovesosoi.serial_android.bean.SerialConfig;
import com.lovesosoi.serial_android.constants.CardConstant;
import com.lovesosoi.serial_android.constants.DeviceConstant;
import com.lovesosoi.serial_android.listener.IDeviceControl;
import com.lovesosoi.serial_android.listener.IReadCardListener;
import com.lovesosoi.serial_android.util.MConfig;
import com.lovesosoi.serial_android.util.SerialUtils;

public class TCardProvider extends MSerialPort   {
    public static final String TAG = "DevicePort";

    public static TCardProvider getInstance() {
        if (instance == null) {
            synchronized (TCardProvider.class) {
                if (instance == null) {
                    instance = new TCardProvider();
                }
            }
        }
        return instance;
    }

    private static volatile TCardProvider instance;

    public TCardProvider() {
        super(new SerialConfig(MConfig.getApp(), CardConstant.DEVICE_CONFIG_NAME),status -> {
            if (status) {
                Log.e(TAG, DeviceConstant.DEVICE_CONFIG_NAME + "初始化失败");
            } else {
                Log.e(TAG, DeviceConstant.DEVICE_CONFIG_NAME + "初始化成功");
            }

        });

    }

    private IReadCardListener iReadCardListener;

    public IReadCardListener getiReadCardListener() {
        return iReadCardListener;
    }
    public void setiReadCardListener(IReadCardListener iReadCardListener) {
        this.iReadCardListener = iReadCardListener;
    }

    @Override
    protected void recvData(byte[] buffer, int size) {
        super.recvData(buffer, size);
        byte[] recBuf = copyArray(buffer, size);
        String cardNum = SerialUtils.parserByteToCardNoTx260t(recBuf);
        if (iReadCardListener!=null){
            iReadCardListener.upCardNum(cardNum);
        }
    }


}