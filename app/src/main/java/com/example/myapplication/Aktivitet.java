package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.widget.ImageView;
import android.media.MediaPlayer;




public class Aktivitet extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Vibrator vibrator;
    private ImageView movingFigure;
    private MediaPlayer mediaPlayer;
    private static final double SHAKE_THRESHOLD = 10.0;
    private int screenWidth;
    private int screenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivitet);

        //Initialiserar sensor manager, accelerometer, vibrator, mediaplayer och movingFigure
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        movingFigure = findViewById(R.id.imageView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registerar accelerometerns sensor listener
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Avregistrerar accelerometerns sensor listener för att spara resurser
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Kollar om sensor typen är accelerometer
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Hämtar accelerometer värdena
            float x = event.values[0];
            float y = event.values[1];

            // Uppdaterar positionen på figuren baserat på accelerometernsvärde.Update the position of the moving figure based on accelerometer values
            moveFigure(x, y);


            double acceleration = Math.sqrt(x * x + y * y);
            //Vibrerar om telefonen lutar med än värdet
            if (acceleration > 7) {
                vibrate(x, y);
            }
            //Spelar ljudet om telefonen skakar mer än värdet
            if(acceleration > SHAKE_THRESHOLD){
                playSound();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Har inte behövt denna för min lösning men metoden måste finnas
    }

   /* private void vibrate() {
        // Vibrerar 500 millisekunder
        vibrator.vibrate(500);
    }*/



    private void vibrate(float x, float y) {
        // Calculate the distance of the figure from the edges of the phone
        float distanceToLeftEdge = movingFigure.getX();
        float distanceToRightEdge = screenWidth - movingFigure.getX() - movingFigure.getWidth();
        float distanceToTopEdge = movingFigure.getY();
        float distanceToBottomEdge = screenHeight - movingFigure.getY() - movingFigure.getHeight();

        // Ändrar intensiteten av vibrationen beroende på avstånd till kanten
        int intensity = calculateVibrationIntensity(distanceToLeftEdge, distanceToRightEdge, distanceToTopEdge, distanceToBottomEdge);

        // Vibrerat med given intensitet
        vibrator.vibrate(intensity);
    }

    private int calculateVibrationIntensity(float left, float right, float top, float bottom) {
        // Ökar vibrations intensiteten när figuren närmar sig kanten.
        float maxDistance = Math.max(left, Math.max(right, Math.max(top, bottom)));
        int maxVibration = 500; // Max vibration
        return (int) (maxVibration * (maxDistance / screenWidth));
    }

    private void moveFigure(float x, float y) {
        // Kalkylerar en ny position för figuren baserat på accelerometervärdena
        float newX = movingFigure.getX() - x;
        float newY = movingFigure.getY() + y;

        // Ändrar så att figuren inte kan lämna skärmen
        newX = Math.max(0, Math.min(newX, screenWidth - movingFigure.getWidth()));
        newY = Math.max(0, Math.min(newY, screenHeight - movingFigure.getHeight()));


        // Uppdaterar positionen på figuren
        movingFigure.setX(newX);
        movingFigure.setY(newY);
    }

    private void playSound() {
        // Startar ljudet
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    //Skickar tillbaka till "hemskärmen"
    public void launchHome(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }



}