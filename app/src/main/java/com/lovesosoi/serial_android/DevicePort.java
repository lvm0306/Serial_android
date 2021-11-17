package com.lovesosoi.serial_android;

import android.app.Application;
import android.nfc.Tag;
import android.util.Log;

import com.lovesosoi.serial_android.bean.SerialConfig;
import com.lovesosoi.serial_android.constants.DeviceConstant;
import com.lovesosoi.serial_android.listener.IDeviceControl;
import com.lovesosoi.serial_android.listener.ISerialPort;
import com.lovesosoi.serial_android.util.MConfig;
import com.lovesosoi.serial_android.util.SerialUtils;

import android_serialport_api.SerialPort;

public class DevicePort extends MSerialPort implements IDeviceControl {
    public static final String TAG = "DevicePort";

    public static DevicePort getInstance() {
        if (instance == null) {
            synchronized (DevicePort.class) {
                if (instance == null) {
                    instance = new DevicePort();
                }
            }
        }
        return instance;
    }

    private static volatile DevicePort instance;

    public DevicePort() {
        super(new SerialConfig(MConfig.getApp(), DeviceConstant.DEVICE_CONFIG_NAME), new ISerialPort() {
            @Override
            public void initStatus(boolean status) {
                if (status) {
                    Log.e(TAG, DeviceConstant.DEVICE_CONFIG_NAME + "初始化失败");
                } else {
                    Log.e(TAG, DeviceConstant.DEVICE_CONFIG_NAME + "初始化成功");
                }

            }
        });

    }

    @Override
    public void openDoor() {
        sendToSerial(DeviceConstant.OPEN_LIGHT_CMD);
    }

    @Override
    public void closeDoor() {

    }

    @Override
    public void doOpenDoor() {

    }

    @Override
    public void doRcvCircleCheck(byte[] s) {

    }

    @Override
    public void doPreventSplit(byte[] s) {

    }

    @Override
    public void doDoorMagnet(byte[] s) {

    }


    @Override
    protected void recvData(byte[] buffer, int size) {
        super.recvData(buffer, size);
        if (buffer.length < 4) return;
        byte[] recBuf = copyArray(buffer, size);
        int cmdtype = recBuf[3];
        String bufString = SerialUtils.bytesToHex(recBuf);
        Log.d(TAG, "buf:" + bufString + "," + "cmdtype: " + cmdtype);
        //检测队列
        receiveReply(buffer);
        switch (cmdtype) {
            case DeviceConstant.RCV_CMD_OPENDOOR:
                doOpenDoor();
                break;
            case DeviceConstant.RCV_CMD_CIRCLECHECK:
                doRcvCircleCheck(recBuf);
                break;
            case DeviceConstant.RCV_CMD_PRESENT_SPLIT://防拆
                doPreventSplit(recBuf);
                break;
            case DeviceConstant.RCV_CMD_DOORMAGNET://门磁
                doDoorMagnet(recBuf);
                break;
        }
    }
}