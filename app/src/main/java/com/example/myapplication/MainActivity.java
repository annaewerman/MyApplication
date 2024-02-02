package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.text.DecimalFormat;

import android.widget.TextView;






public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView accelerationTextView;
    private Button myButton;
    private boolean isTextViewVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instansierar de saker som behövs
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerationTextView = findViewById(R.id.accelerationTextView);
        accelerationTextView.setVisibility(View.INVISIBLE);
        myButton = findViewById(R.id.buttonacc);
        isTextViewVisible = false;

        // Sätter en click listener på knappen
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Visar accelerometer värdena när knappen klickas på
                displayAccelerometerValues();
            }
        });

    }
    final SensorEventListener sensorListener = new SensorEventListener() {
        private DecimalFormat decimalFormat = new DecimalFormat("#.###");
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Visar accelerometer värden i TextView
            accelerationTextView.setText("Acceleration: \n X: " + decimalFormat.format(x) + "\n Y: " + decimalFormat.format(y) + "\nZ: " + decimalFormat.format(z));

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //Behöver finnas men används inte
        }
    };

    @Override
    protected void onResume() {
        //Registrerar accelerometerns sensor listener
        super.onResume();
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Avregistrerar accelerometerns sensor listener för att spara resurser
        super.onPause();
        sensorManager.unregisterListener(sensorListener);
    }


    private void displayAccelerometerValues() {
        if (isTextViewVisible) {
            // Om TextView är visible, gör det invisible
            accelerationTextView.setVisibility(View.INVISIBLE);
        } else {
            // Om TextView är invisible, gör det visible
            accelerationTextView.setVisibility(View.VISIBLE);
        }

        // Ändrar booleanen
        isTextViewVisible = !isTextViewVisible;
    }





//Startar aktiviteten
    public void launchAktivitet(View v){
        Intent i = new Intent(this, Aktivitet.class);
        startActivity(i);

    }


}