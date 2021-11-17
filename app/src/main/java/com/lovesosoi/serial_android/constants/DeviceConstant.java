package com.lovesosoi.serial_android.constants;

public class DeviceConstant {
    public static final String DEVICE_CONFIG_NAME="device_config.json";
    public final static byte[] CLOSE_LIGHT_CMD = new byte[]{0x55, 0x00, 0x02, 0x02, 0x00, 0x57};
    public final static byte[] OPEN_LIGHT_CMD = new byte[]{0x55, 0x00, 0x02, 0x02, 0x01, 0x58};
    public static final int RCV_CMD_OPENDOOR = 0x04;//出门开关
    public static final int RCV_CMD_CIRCLECHECK = 0x08;//板子开启状态开关
    public static final int RCV_CMD_PRESENT_SPLIT = 0x07;//防拆
    public static final int RCV_CMD_DOORMAGNET = 0x05;//门磁
//    private final static byte[] OPENDOOR_CMD = new byte[]{0x55, 0x00, 0x02, 0x01, 0x00, 0x56};
//    private final static byte[] CLOSEDOOR_CMD = new byte[]{0x55, 0x00, 0x02, 0x01, 0x01, 0x57};
//    private final static byte[] CIRCLECHECK_CMD = new byte[]{0x55, 0x00, 0x02, 0x08, 0x00, 0x5D};
//    private final static byte[] CLOSE_LIGHT_CMD = new byte[]{0x55, 0x00, 0x02, 0x02, 0x00, 0x57};
//    private final static byte[] OPEN_LIGHT_CMD = new byte[]{0x55, 0x00, 0x02, 0x02, 0x01, 0x58};
}
