package com.example.womensafetyalaramapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.ArrayList;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.SEND_SMS;

public class MainActivity extends AppCompatActivity {
    Button btnAddContact,btnEmergency,btnSiren;
    DatabaseHandler myDB;
    String x="",y="";
    private static final int REQUEST_LOCATION=1;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddContact=findViewById(R.id.btnAddContact);
        btnEmergency=findViewById(R.id.btnEmergency);
        btnSiren=findViewById(R.id.btnSiren);
        myDB = new DatabaseHandler(this);

        MediaPlayer mp=MediaPlayer.create(getApplicationContext(),R.raw.warning);
        locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) startTrack();

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,Contacts.class);
                startActivity(i);
            }
        });
        btnSiren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                mp.setLooping(true);
            }
        });

        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                call();
            }

        });


    }

    private void loadData() {
        ArrayList<String> lists = new ArrayList<>();
        Cursor data=myDB.getListContents();
        if(data.getCount()==0){
            Toast.makeText(
                    MainActivity.this,
                    "No contacts available. Please add a Contact Number.",
                    Toast.LENGTH_LONG).show();
        }
        else{
            String msg="I NEED HELP AT Latitude:"+x+" and Logitude:"+y;
            String Number="";
            while(data.moveToNext()){
                lists.add(data.getString(1));
                Number=Number+data.getString(1)+(data.isLast()?"":";");

            }
            if(!lists.isEmpty()){
                sendSMS(Number,msg);
            }

        }
    }

    private void sendSMS(String number, String msg) {
        Intent smsIntent=new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+number));
        smsIntent.putExtra("sms_body",msg);
        if(ContextCompat.checkSelfPermission(getApplicationContext(),SEND_SMS)== PackageManager.PERMISSION_GRANTED){
            startActivity(smsIntent);
        }else {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{SEND_SMS},1);
            }
        }
    }

    private void call() {
        Intent callIntent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:10000"));
        if(ContextCompat.checkSelfPermission(getApplicationContext(),CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
            startActivity(callIntent);
        }
        else {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{CALL_PHONE},1);
            }
        }
    }

    private void startTrack() {
        if(ActivityCompat.
                checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },REQUEST_LOCATION);
        }
        else{
            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null){
                double lat=location.getLatitude();
                double lon=location.getLongitude();
                x=String.valueOf(lat);
                y=String.valueOf(lon);
            }
            else {
                Toast.makeText(
                        MainActivity.this,
                        "Enable to find Location.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


}