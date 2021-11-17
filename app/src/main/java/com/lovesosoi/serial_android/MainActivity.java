package com.lovesosoi.serial_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.lovesosoi.serial_android.util.MConfig;

public class MainActivity extends AppCompatActivity {
    MSerialPort mSerialPort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
//        SerialPortUtils.getInstance().init();
        MConfig.init(getApplication());
    }


    public void circleCheck(View view) {
    }

    public void openLight(View view) {
//        SerialPortUtils.getInstance().openLight();
//        mSerialPort.openLight();
    }

    public void closeLight(View view) {
//        SerialPortUtils.getInstance().closeLight();
//        mSerialPort.closeLight();

    }

    public void openDoor(View view) {

    }
}
