package com.example.sensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {
    public static final String KEY_EXTRA_SENSOR_TYPE = "";

    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private RecyclerView recyclerView;
    private SensorAdapter adapter;
    private boolean subtitleVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if(adapter == null) {
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        }
        else {
            adapter.notifyDataSetChanged();
        }

        for (Sensor sensor : sensorList) {
            Log.d("ZADANIE5", "SENSOR: " + sensor.getName() + " ; " + sensor.getVendor() + " ; " + sensor.getMaximumRange());
        }
    }

    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView sensorNameTextView;
        private ImageView sensorIconImageView;
        private Sensor sensor;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            itemView.setOnClickListener(this);

            sensorNameTextView = itemView.findViewById(R.id.text_sensor_name);
            sensorIconImageView = itemView.findViewById(R.id.icon_sensor_symbol);
        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            sensorNameTextView.setText(sensor.getName());

            if (sensor.getType() == Sensor.TYPE_LIGHT || sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                sensorIconImageView.setImageResource(R.drawable.ic_sensor_declared);
            }
            else
            {
                sensorIconImageView.setImageResource(R.drawable.ic_sensor);
            }
        }

        @Override
        public void onClick(View v) {
            //Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
            int correctAnswer = sensor.getType();
            //intent.putExtra(KEY_EXTRA_SENSOR_TYPE, correctAnswer);
            if(correctAnswer == Sensor.TYPE_MAGNETIC_FIELD) {
                Intent intent = new Intent(SensorActivity.this, LocationActivity.class);
                intent.putExtra(KEY_EXTRA_SENSOR_TYPE, correctAnswer);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                intent.putExtra(KEY_EXTRA_SENSOR_TYPE, correctAnswer);
                startActivity(intent);
            }
            //startActivity(intent);
        }
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private List<Sensor> sensors;

        public SensorAdapter(List<Sensor> sensorList) {
            this.sensors = sensorList;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new SensorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensors.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        this.getMenuInflater().inflate(R.menu.sensor_menu, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_sensors_count);

        if (subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_sensors_count:
                subtitleVisible = !subtitleVisible;
                this.invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateSubtitle()
    {
        String subtitle = getString(R.string.sensors_count, sensorList.size());
        if (!subtitleVisible)
        {
            subtitle = null;
        }
        this.getSupportActionBar().setSubtitle(subtitle);
    }
}