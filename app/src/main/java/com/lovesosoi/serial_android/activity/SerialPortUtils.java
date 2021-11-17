package com.lovesosoi.serial_android.activity;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import android_serialport_api.SerialPort;

public class SerialPortUtils {

    private final static byte[] OPENDOOR_CMD = new byte[]{0x55, 0x00, 0x02, 0x01, 0x00, 0x56};
    private final static byte[] CLOSEDOOR_CMD = new byte[]{0x55, 0x00, 0x02, 0x01, 0x01, 0x57};
    private final static byte[] CIRCLECHECK_CMD = new byte[]{0x55, 0x00, 0x02, 0x08, 0x00, 0x5D};
    private final static byte[] CLOSE_LIGHT_CMD = new byte[]{0x55, 0x00, 0x02, 0x02, 0x00, 0x57};
    private final static byte[] OPEN_LIGHT_CMD = new byte[]{0x55, 0x00, 0x02, 0x02, 0x01, 0x58};
    private static final String TAG = SerialPortUtils.class.getCanonicalName();
    private static volatile SerialPortUtils instance;
    private String mSerialFile = "/dev/ttyS3";
    private int mBaudRate = 115200;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private int mBufsize = 2048;
    private long lastSendTime = 0;
    private long MAX_WAIT = 200;
    private byte lastSendCmd = 0x0;
    private static LinkedBlockingQueue<byte[]> mCmdLinkedQueue = new LinkedBlockingQueue<byte[]>();

    public static SerialPortUtils getInstance() {
        if (instance == null) {
            synchronized (SerialPortUtils.class) {
                if (instance == null) {
                    instance = new SerialPortUtils();
                }
            }
        }
        return instance;
    }

    static {
        System.loadLibrary("serial_port");
    }


    public void init() {

        new Thread() {
            @Override
            public void run() {
                SerialPort mSerialPort;
                try {
                    mSerialPort = new SerialPort(new File(mSerialFile), mBaudRate, 0);
                } catch (SecurityException var3) {
                    var3.printStackTrace();

                    return;
                } catch (IOException var4) {
                    var4.printStackTrace();
                    return;
                }
                if (mSerialPort == null) {
                    Log.d(TAG, mSerialFile + " 串口初始化失败");
                    return;
                } else {
                    Log.d(TAG, mSerialFile + " 串口初始化成功");
                    mInputStream = mSerialPort.getInputStream();
                    mOutputStream = mSerialPort.getOutputStream();
                    Thread thread = new Thread(readRunable);
                    thread.start();
                }


            }
        }.start();
    }

    Runnable readRunable = new Runnable() {
        boolean flag = true;


        @Override
        public void run() {
            flag = true;

            while (this.flag) {
                if (mInputStream == null) {
                    return;
                }
                try {
                    if (mInputStream.available() > 0) {
                        byte[] buffer = new byte[mBufsize];
                        int size = mInputStream.read(buffer);
                        if (size > 0) {
                            recvData(buffer, size);
                        }
                    }
                } catch (IOException var4) {
                    var4.printStackTrace();
                }

                try {
                    Thread.currentThread();
                    Thread.sleep(100L);
                } catch (InterruptedException var3) {
                    var3.printStackTrace();
                }
            }

        }
    };


    protected void recvData(byte[] buffer, int size) {
    }

    public void sendCmd(byte[] cmd) {
        sendToSerial(cmd);
    }


    public void sendToSerial(byte[] cmd) {
        if (cmd.length < 6) return;
        long nowTime = System.currentTimeMillis();
        long bt = nowTime - lastSendTime;
        if (bt < MAX_WAIT) {//加入等待队列
            Log.d(TAG, "add cmd to queen");
            putToQueen(cmd);
        } else {
            Log.d(TAG, "send cmd now: ");
            sendCmd2(cmd);
        }
    }

    private void sendCmd2(byte[] cmd) {
        lastSendTime = System.currentTimeMillis();
        lastSendCmd = cmd[3];
        Log.d(TAG, "send cmd type" + lastSendCmd);
        try {
            mOutputStream.write(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putToQueen(byte[] cmd) {
        try {
            mCmdLinkedQueue.put(cmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void openLight() {
        Log.d(TAG, "openLight() called");
        sendCmd(OPEN_LIGHT_CMD);
    }

    public void closeLight() {
        Log.d(TAG, "closeLight: ");
        sendCmd(CLOSE_LIGHT_CMD);
    }
}
