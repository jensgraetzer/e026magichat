package com.example.graetzer.e026magichat;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private ImageView img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img1 = findViewById(R.id.imageView);
        img1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showTheOtherImage();
            }
        });

        // Get an instance of the sensor service, and use that to get
        // an instance of a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
    }

    void showTheOtherImage() {
        // Make img1 disappear for 2 Seconds - that shows img2
        img1.setVisibility(View.GONE);
        Log.i(TAG, "img1 has GONE");

        new CountDownTimer(2000, 500) {
            public void onTick(long millisUntilFinished) {
                // do nothing
                Log.i(TAG, "CountDownTimer onTick");
            }
            public void onFinish() {
                img1.setVisibility(View.VISIBLE);
                Log.i(TAG, "CountDownTimer onFinish: img1 is VISIBLE");
            }
        }.start();
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
        Log.i(TAG, "onAccuracyChanged()");
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        Log.i(TAG, "onSensorChanged()");
        double tu = 130;  // upper shake threshold - must be bigger than 9.81*9.81
        double tl = 65;   // lower shake threshold - must be lower than 9.81*9.81
        double gValueSquared = Math.pow(event.values[0], 2) +
                Math.pow(event.values[1], 2) +
                Math.pow(event.values[2], 2);
        if ((gValueSquared > tu || gValueSquared < tl)
                && (img1.getVisibility() == View.VISIBLE)) {
            Log.i(TAG, "Shake Event detected.");
            showTheOtherImage();
        }
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this,
                mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
