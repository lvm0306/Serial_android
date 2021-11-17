package com.lovesosoi.serial_android.listener;

public interface IDeviceControl {
    void openDoor();

    void closeDoor();

    void doOpenDoor();
    void doRcvCircleCheck(byte[] s);
    void doPreventSplit(byte[] s);
    void doDoorMagnet(byte[] s);
}
