package com.lovesosoi.serial_android.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SerialUtils {
    /**
     * 获取assets 下的文件
     *
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context mContext, String fileName) {
        String Result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(mContext.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;

    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String parserByteToCardNoTx260t(byte[] bs) {
        if (bs == null || bs.length < 12) return "";
        byte[] buf = new byte[8];
        System.arraycopy(bs, 3, buf, 0, buf.length);
        String num = bytesToHex(buf);
        return num;
    }
}
