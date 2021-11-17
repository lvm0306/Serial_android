package com.lovesosoi.serial_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lovesosoi.serial_android.R;
import com.lovesosoi.serial_android.base.MSerialPort;
import com.lovesosoi.serial_android.listener.IReadCardListener;
import com.lovesosoi.serial_android.temp.TCardProvider;
import com.lovesosoi.serial_android.temp.TDeviceProvider;
import com.lovesosoi.serial_android.util.MConfig;

public class MainActivity extends AppCompatActivity {
    MSerialPort mSerialPort;
    TDeviceProvider tDeviceProvider;
    TCardProvider tCardProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        MConfig.init(getApplication());
        tDeviceProvider = TDeviceProvider.getInstance();

    }


    public void circleCheck(View view) {
    }

    public void openLight(View view) {
        tDeviceProvider.openLight();
    }

    public void closeLight(View view) {
        tDeviceProvider.closeLight();
    }

    public void openDoor(View view) {
        tCardProvider = TCardProvider.getInstance();
        tCardProvider.setiReadCardListener(new IReadCardListener() {
            @Override
            public void upCardNum(String cardNum) {
                Log.e("MainActivity", "cardNum===>" + cardNum);
            }
        });
    }
}
