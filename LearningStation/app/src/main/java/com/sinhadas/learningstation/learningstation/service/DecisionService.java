package com.sinhadas.learningstation.learningstation.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sinhadas.learningstation.learningstation.DataCallBack;

/**
 * Created by dipankardas on 15/08/16.
 */


public class DecisionService extends Service  implements SensorEventListener{
    private static final String TAG = DecisionService.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mHRMSensor;
    private DataCallBack mDataCallback;



    public class LocalBinder extends Binder{
        public DecisionService getService(){
            return DecisionService.this;
        }
    }


    @Nullable
    @Override



    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    float v  = event.values[0];


    Log.d(TAG, "HRM values: " +v);


        mDataCallback.uiUpdate(v);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    private void registerSensors(){

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mHRMSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        Log.d(TAG, "Heart rate sensor "+(mHRMSensor==null));
        mSensorManager.registerListener(this,mHRMSensor,SensorManager.SENSOR_DELAY_NORMAL);


    }

    private void deregisterSensors(){
        mSensorManager.unregisterListener(this);
    }

    public void startSensors(DataCallBack callBack){
        mDataCallback = callBack;
        Log.d(TAG, "Start Sensors");
        registerSensors();
    }
    public void stopSensors(){
        deregisterSensors();
    }




}
