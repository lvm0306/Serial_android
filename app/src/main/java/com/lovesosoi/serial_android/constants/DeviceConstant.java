package com.lovesosoi.serial_android.constants;

public class DeviceConstant {
    public static final String DEVICE_CONFIG_NAME="device_config.json";
    public final static byte[] CLOSE_LIGHT_CMD = new byte[]{0x55, 0x00, 0x02, 0x02, 0x00, 0x57};
    public final static byte[] OPEN_LIGHT_CMD = new byte[]{0x55, 0x00, 0x02, 0x02, 0x01, 0x58};
    public final static byte[] OPENDOOR_CMD = new byte[]{0x55, 0x00, 0x02, 0x01, 0x00, 0x56};
    public final static byte[] CLOSEDOOR_CMD = new byte[]{0x55, 0x00, 0x02, 0x01, 0x01, 0x57};
    public final static byte[] CIRCLECHECK_CMD = new byte[]{0x55, 0x00, 0x02, 0x08, 0x00, 0x5D};
}
