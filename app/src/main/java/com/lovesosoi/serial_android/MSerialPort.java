package com.lovesosoi.serial_android;

import android.util.Log;

import com.google.gson.Gson;
import com.lovesosoi.serial_android.bean.SerialConfig;
import com.lovesosoi.serial_android.bean.SerialBean;
import com.lovesosoi.serial_android.listener.ISerialPort;
import com.lovesosoi.serial_android.util.AppExecutors;
import com.lovesosoi.serial_android.util.SerialUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import android_serialport_api.SerialPort;

public class MSerialPort {

    private static final String TAG = "MSerialPort";
    private static volatile MSerialPort instance;

    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private SerialConfig mConfig;
    protected ISerialPort iSerialPort;
    private SerialBean mSerialBean;

    public MSerialPort(SerialConfig mConfig, ISerialPort iSerialPort) {
        this.mConfig = mConfig;
        this.iSerialPort = iSerialPort;
        init();
    }

    public SerialBean getSerialBean() {
        return mSerialBean;
    }

    public void setSerialBean(SerialBean mBean) {
        this.mSerialBean = mBean;
    }

    public SerialConfig getConfig() {
        return mConfig;
    }

    public void setConfig(SerialConfig mBean) {
        this.mConfig = mBean;
    }

    public ISerialPort getiSerialPort() {
        return iSerialPort;
    }

    public void setiSerialPort(ISerialPort iSerialPort) {
        this.iSerialPort = iSerialPort;
    }

    public void init() {
        String json = SerialUtils.getFromAssets(mConfig.getContext(), mConfig.getConfigName());
        mSerialBean = new Gson().fromJson(json, SerialBean.class);
        Log.e(TAG, "init: " + getSerialBean().toString());
        AppExecutors.getInstance().networkIO().execute(new SerialInitRunanble());
    }

    class SerialInitRunanble implements Runnable {
        @Override
        public void run() {
            try {
                SerialPort mSerialPort;
                try {
                    mSerialPort = new SerialPort(new File(getSerialBean().getSerialFile()), getSerialBean().getBaudRate(), 0);
                } catch (SecurityException | IOException var3) {
                    var3.printStackTrace();
                    iSerialPort.initStatus(false);
                    Log.d(TAG, getSerialBean().getSerialFile() + " 串口初始化失败");
                    return;
                }
                iSerialPort.initStatus(true);
                mInputStream = mSerialPort.getInputStream();
                mOutputStream = mSerialPort.getOutputStream();
                Log.d(TAG, getSerialBean().getSerialFile() + " 串口初始化成功");
                AppExecutors.getInstance().networkIO().execute(new SerialInit2Runnable());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class SerialInit2Runnable implements Runnable {
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
                        byte[] buffer = new byte[getSerialBean().getBufsize()];
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
    }

    private byte lastRcvCmd = 0x00;
    private long lastSendTime = 0;
    private long MAX_WAIT = 200;
    private byte lastSendCmd = 0x0;
    private static LinkedBlockingQueue<byte[]> mCmdLinkedQueue = new LinkedBlockingQueue<byte[]>();

    public void sendToSerial(byte[] cmd) {
        if (checkCmd(cmd)) return;
        if (isAddWait()) {//加入等待队列
            putToQueen(cmd);
        } else {
            sendCmd(cmd);
        }
    }

    //检测cmd 合法性
    private boolean checkCmd(byte[] cmd) {
        if (cmd.length < 6) return false;
        else return true;
    }

    //是否加入等待队列
    private boolean isAddWait() {
        long nowTime = System.currentTimeMillis();
        long bt = nowTime - lastSendTime;
        return bt < MAX_WAIT;
    }

    private void doSendCmdInQueen() {
        byte[] cmd = mCmdLinkedQueue.poll();
        Log.d(TAG, "the queen last cmd:" + cmd[3] + " the queen size:" + mCmdLinkedQueue.size());
        if (cmd != null) {
            sendCmd(cmd);
        }
    }

    private void putToQueen(byte[] cmd) {
        Log.d(TAG, "add cmd to queen");
        try {
            mCmdLinkedQueue.put(cmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //
    private void sendCmd(byte[] cmd) {
        Log.d(TAG, "send cmd now: ");
        lastSendTime = System.currentTimeMillis();
        lastSendCmd = cmd[3];
        Log.d(TAG, "send cmd type" + lastSendCmd);
        try {
            mOutputStream.write(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected byte[] copyArray(byte[] buffer, int size) {
        byte[] recBuf = new byte[size];
        System.arraycopy(buffer, 0, recBuf, 0, size);
        return recBuf;
    }

    protected void recvData(byte[] buffer, int size) {
//        byte[] recBuf = copyArray(buffer, size);
//        if (recBuf.length < 4) return;
//        int cmdtype = buffer[3];
//        String bufString = SerialUtils.bytesToHex(recBuf);
//        Log.d(TAG, "buf:" + bufString+","+"cmdtype: " + cmdtype);
//        if (mSendSerialManager != null) mSendSerialManager.receiveReply(buffer);
    }

    protected void receiveReply(byte[] rcvCmd) {
        lastSendTime = 0L;
        if (rcvCmd != null && rcvCmd.length > 3)
            lastRcvCmd = rcvCmd[3];
        Log.d(TAG, "receive cmd:" + lastRcvCmd);
        //检查等待队列
        if (!mCmdLinkedQueue.isEmpty()) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    doSendCmdInQueen();
                }
            });

        }
    }


}
