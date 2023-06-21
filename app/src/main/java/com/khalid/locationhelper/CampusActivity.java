package com.khalid.locationhelper;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Locale;

public class CampusActivity extends AppCompatActivity implements SensorEventListener {



    private static SensorManager sensorService;
    TextView currLoc;
    private float currentDegree = 0.0f;
    ImageView imageViewCompass;
    LocationListener locationListener;
    LocationManager locationManager;
    private Sensor sensor;
    String text;
    TextToSpeech tts;
    TextView tv_degrees;


    Button addLocations;

    int degreeCheckFace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus);
        this.currLoc = (TextView) findViewById(R.id.currLoc);
        this.imageViewCompass = (ImageView) findViewById(R.id.imageViewCompass);
        this.tv_degrees = (TextView) findViewById(R.id.tv_degrees);
       // addLocations = findViewById(R.id.addLocation);

       // addLocations.setOnClickListener(new View.OnClickListener() {
        //    @Override
         //   public void onClick(View view) {

             //   startActivity(new Intent(CampusActivity.this,login.class));

          //  }
      //  });


        sensorService = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.sensor = sensorService.getDefaultSensor(3);

        this.tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                if (status == 0) {
                    int result = CampusActivity.this.tts.setLanguage(Locale.UK);
                    if (result == -1 || result == -2) {
                        Log.e("error", "This Language is not supported");
                    }
                    return;
                }
                Log.e("error", "Initilization Failed!");
            }
        });
        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        }
        this.locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                final double d = latitude;
                final double longitude = location.getLongitude();
                FirebaseDatabase.getInstance().getReference().child("Locations").addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //  AnonymousClass1 anonymousClass1 = this;

                        try {
                            Iterator it = dataSnapshot.getChildren().iterator();
                            while (it.hasNext()) {
                                DataSnapshot snapshot = (DataSnapshot) it.next();
                                String DBlatitude = snapshot.child("latitude").getValue().toString();
                                String DBlongitude = snapshot.child("longitude").getValue().toString();
                                String north = snapshot.child("North").getValue().toString();
                                String south = snapshot.child("South").getValue().toString();
                                String east = snapshot.child("East").getValue().toString();
                                String west = snapshot.child("West").getValue().toString();


                                String centre = snapshot.child("current").getValue().toString();
                                Location areaOfIinterest = new Location("locationListener");
                                Location currentPosition = new Location("locationListener");
                                areaOfIinterest.setLatitude(Double.parseDouble(DBlatitude));
                                areaOfIinterest.setLongitude(Double.parseDouble(DBlongitude));
                                currentPosition.setLatitude(d);
                                currentPosition.setLongitude(longitude);
                                float dist = areaOfIinterest.distanceTo(currentPosition);
                                boolean isWithin10m = dist < 20.5f;
                                Context applicationContext = CampusActivity.this.getApplicationContext();
                                StringBuilder stringBuilder = new StringBuilder();
                                Iterator it2 = it;
                                stringBuilder.append("retrived firebase location: "+centre+"");
                                stringBuilder.append(DBlatitude);
                                stringBuilder.append(", ");
                               stringBuilder.append(DBlongitude);
                                Toast.makeText(applicationContext, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                                Context applicationContext2 = CampusActivity.this.getApplicationContext();
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("distance is: ");
                                stringBuilder.append(dist);
                                stringBuilder.append(", boolean is; ");
                                stringBuilder.append(isWithin10m);
                                Toast.makeText(applicationContext2, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                                Log.i("disttt",""+dist);
                                if (isWithin10m) {

                              //    int degree =  Integer.parseInt(tv_degrees.getText().toString());

                                    TextView textView = CampusActivity.this.currLoc;
                                    stringBuilder = new StringBuilder();
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("Your are standing at: ");
                                    stringBuilder2.append(centre);
                                    stringBuilder2.append(",Your east is: ");
                                    stringBuilder2.append(east);
                                    stringBuilder2.append(" Your west is: ");
                                    stringBuilder2.append(west);
                                    stringBuilder.append(stringBuilder2.toString());
                                    stringBuilder.append(" Your north is: ");
                                    stringBuilder.append(north);
                                    stringBuilder.append(" Your south is: ");
                                    stringBuilder.append(south);


                                    if(degreeCheckFace > 0 && degreeCheckFace < 90 ) {
                                        stringBuilder.append(" your face is towards North East");

                                    }
                                    if(degreeCheckFace > 90 && degreeCheckFace < 180 ) {
                                        stringBuilder.append(" your face is towards South East");

                                    }
                                    if(degreeCheckFace > 180 && degreeCheckFace < 270 ) {
                                        stringBuilder.append(" your face is towards South West");

                                    }
                                    if(degreeCheckFace > 270 && degreeCheckFace < 345 ) {
                                        stringBuilder.append(" your face is towards North West");

                                    }
                                    if(degreeCheckFace == 0 ) {
                                        stringBuilder.append(" your face is towards North");

                                    }
                                    if(degreeCheckFace == 90 ) {
                                        stringBuilder.append(" your face is towards East");

                                    }
                                    if(degreeCheckFace == 180 ) {
                                        stringBuilder.append(" your face is towards South");

                                    }
                                    if(degreeCheckFace == 270 ) {
                                        stringBuilder.append(" your face is towards West");

                                    }
                                    textView.setText(stringBuilder.toString());
                                    CampusActivity.this.ConvertTextToSpeech();
                                    return;
                                }
                                it = it2;
                            }
                        }catch (Exception e){
                            Toast.makeText(CampusActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        this.locationManager.requestLocationUpdates("gps", 3000, 3.0f, this.locationListener);


    }



    protected void onPause() {
        if (this.tts != null) {
            this.tts.stop();
            this.tts.shutdown();
        }
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

    private void ConvertTextToSpeech() {
        this.text = this.currLoc.getText().toString();
        if (this.text != null) {
            if (!"".equals(this.text)) {
                this.tts.setSpeechRate(0.7f);
                TextToSpeech textToSpeech = this.tts;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.text);
                stringBuilder.append("");
                textToSpeech.speak(stringBuilder.toString(), 0, null);
                return;
            }
        }
        this.text = "Content not available";
        this.tts.speak(this.text, 0, null);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        int degree = Math.round(event.values[0]);


        TextView textView = this.tv_degrees;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Integer.toString(degree));

        degreeCheckFace = degree;

        stringBuilder.append('°');
        textView.setText(stringBuilder.toString());
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

                startActivity(new Intent(CampusActivity.this,login.class));
                return true;
            case R.id.b:
                startActivity(new Intent(CampusActivity.this,SignupActivity.class));
                return true;
            case R.id.c:
                startActivity(new Intent(CampusActivity.this,Help.class));
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
