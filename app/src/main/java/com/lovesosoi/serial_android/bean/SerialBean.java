package com.lovesosoi.serial_android.bean;

import java.io.Serializable;

public class SerialBean implements Serializable {

    @Override
    public String toString() {
        return "SerialBean{" +
                "serialFile='" + serialFile + '\'' +
                ", baudRate=" + baudRate +
                ", flags=" + flags +
                ", bufsize=" + bufsize +
                '}';
    }

    private String serialFile;
    private int baudRate;
    private int flags;
    private int bufsize;

    public String getSerialFile() {
        return serialFile;
    }

    public void setSerialFile(String serialFile) {
        this.serialFile = serialFile;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getBufsize() {
        return bufsize;
    }

    public void setBufsize(int bufsize) {
        this.bufsize = bufsize;
    }
}
