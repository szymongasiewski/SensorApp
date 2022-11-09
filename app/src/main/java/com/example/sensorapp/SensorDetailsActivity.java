package com.example.sensorapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorName;
    private TextView sensorValue;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        int sensorType = getIntent().getIntExtra(SensorActivity.KEY_EXTRA_SENSOR_TYPE, 0);

        sensorName = findViewById(R.id.sensor_name);
        sensorValue = findViewById(R.id.sensor_value);
        layout = findViewById(R.id.sensor_details_layout);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);

        if(sensor == null) {
            sensorName.setText(R.string.missing_sensor);
        }
        else {
            sensorName.setText(sensor.getName());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sensor != null)
        {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"StringFormatInvalid", "SetTextI18n"})
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        int red, green, blue;
        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                sensorValue.setText(sensorEvent.values[0] + " lux");
                sensorName.setText(getResources().getString(R.string.light_sensor_label));
                red = (int) (sensorEvent.values[0] / sensorEvent.sensor.getMaximumRange() * 255);
                layout.setBackgroundColor(Color.rgb(red,0,0));
                break;
            case Sensor.TYPE_ACCELEROMETER:
                sensorValue.setText("x: "   + sensorEvent.values[0] + " m/s²" +
                        "\ny: " + sensorEvent.values[1] + " m/s²" +
                        "\nz: " + sensorEvent.values[2] + " m/s²");
                sensorName.setText(getResources().getString(R.string.ac_sensor_label));
                red = (int) (sensorEvent.values[0] / sensorEvent.sensor.getMaximumRange() * 255);
                green = (int) (sensorEvent.values[1] / sensorEvent.sensor.getMaximumRange() * 255);
                blue = (int) (sensorEvent.values[2] / sensorEvent.sensor.getMaximumRange() * 255);
                layout.setBackgroundColor(Color.rgb(red,green,blue));
                break;
            default:
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("ZADANIE5", "onAccuracyChanged");
    }
}