package com.khalid.locationhelper;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class addPointsScreen extends AppCompatActivity implements SensorEventListener {
    Button addPointsBtn;
    TextView centrePoint;
    TextView eastPoint;
    double latt;
    double lngg;
    //  LatLng location;
    TextView northPoint;
    TextView southPoint;
    TextView validationText;
    TextView westPoint;

    ImageView imageViewCompass;

    private float currentDegree = 0.0f;

    private static SensorManager sensorService;
    private Sensor sensor;


    private GET_CURRENT_LOCATION getLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_points_screen);

        getLocation = new GET_CURRENT_LOCATION(addPointsScreen.this);

        sensorService = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.sensor = sensorService.getDefaultSensor(3);


        this.centrePoint = (TextView) findViewById(R.id.centreLocationText);
        this.northPoint = (TextView) findViewById(R.id.northLocationText);
        this.southPoint = (TextView) findViewById(R.id.southLocationText);
        this.imageViewCompass = (ImageView) findViewById(R.id.imageViewCompass);

        this.eastPoint = (TextView) findViewById(R.id.eastLocationText);
        this.westPoint = (TextView) findViewById(R.id.westLocationText);
        this.addPointsBtn = (Button) findViewById(R.id.addPointsBtn);
        this.validationText = (TextView) findViewById(R.id.validationText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.latt = extras.getDouble("currentLat");
            this.lngg = extras.getDouble("currentLng");
        }
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Locations").push();
        this.addPointsBtn.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {


                if (addPointsScreen.this.centrePoint.getText().toString().equals("") || addPointsScreen.this.northPoint.getText().toString().equals("") || addPointsScreen.this.southPoint.getText().toString().equals("") || addPointsScreen.this.eastPoint.getText().toString().equals("") || addPointsScreen.this.westPoint.getText().toString().equals("")) {
                    addPointsScreen.this.validationText.setVisibility(View.VISIBLE);
                    return;
                }
                DatabaseReference child = myRef.child("latitude");
                StringBuilder stringBuilder = new StringBuilder();
                //   stringBuilder.append(addPointsScreen.this.latt);

                stringBuilder.append(getLocation.getLatitude());


                stringBuilder.append("");
                child.setValue(stringBuilder.toString());
                child = myRef.child("longitude");
                stringBuilder = new StringBuilder();

                //  stringBuilder.append(addPointsScreen.this.lngg);

                stringBuilder.append(getLocation.getLongitude());
                Log.i("locatation",""+stringBuilder+"");
                stringBuilder.append("");
                child.setValue(stringBuilder.toString());

                myRef.child("current").setValue(addPointsScreen.this.centrePoint.getText().toString());
                myRef.child("North").setValue(addPointsScreen.this.northPoint.getText().toString());
                myRef.child("South").setValue(addPointsScreen.this.southPoint.getText().toString());
                myRef.child("East").setValue(addPointsScreen.this.eastPoint.getText().toString());
                myRef.child("West").setValue(addPointsScreen.this.westPoint.getText().toString());
                addPointsScreen.this.centrePoint.setText("");
                addPointsScreen.this.northPoint.setText("");
                addPointsScreen.this.southPoint.setText("");
                addPointsScreen.this.eastPoint.setText("");
                addPointsScreen.this.westPoint.setText("");
                addPointsScreen.this.validationText.setVisibility(View.GONE);
            }
        });
    }


    protected void onPause() {

        super.onPause();
        sensorService.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        if (this.sensor != null) {
            sensorService.registerListener(this, this.sensor, 0);
        } else {
            Toast.makeText(getApplicationContext(), "Not Suppoeted", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        int degree = Math.round(event.values[0]);


        //  TextView textView = this.tv_degrees;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Integer.toString(degree));

        //  degreeCheckFace = degree;

        stringBuilder.append('°');
        //textView.setText(stringBuilder.toString());
        RotateAnimation rotateAnimation = new RotateAnimation(this.currentDegree, (float) (-degree), 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        this.imageViewCompass.startAnimation(rotateAnimation);
        this.currentDegree = (float) (-degree);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.a:

                startActivity(new Intent(addPointsScreen.this,addPointsScreen.class));
                return true;
            case R.id.b:
                startActivity(new Intent(addPointsScreen.this,SignupActivity.class));
                return true;
            case R.id.c:
                startActivity(new Intent(addPointsScreen.this,Help.class));
                return true;
            case R.id.d:
                finish();

                System.exit(0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
