package com.example.crisismanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    Button buttonSendLoc;
    String location_str = "http://maps.google.com?q=";
    String note = "";
    ArrayList<String> final_phone = new ArrayList<>();
    View view;
    String temp = "";
    boolean check_base = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button postResources = (Button) findViewById(R.id.button_post_resources);
        Button searchResources = (Button) findViewById(R.id.button_search_resources);
        Button sendlocation = (Button) findViewById(R.id.button_send_location);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                temp = location.getLatitude() + "," + location.getLongitude();
                Log.d("My Location", location_str + temp);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            } else {
                locationManager.requestLocationUpdates("gps", 1000, 1, locationListener);
            }
        } else {
            locationManager.requestLocationUpdates("gps", 1000, 1, locationListener);
        }

        postResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostResources.class);
                startActivity(intent);
            }
        });
        searchResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchResources.class);
                startActivity(intent);
            }
        });
        sendlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, SendLocation.class);
                //startActivity(intent);
                check_base = true;
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[] {
                                  Manifest.permission.READ_CONTACTS,
                                  Manifest.permission.WRITE_CONTACTS
                            }, 10);
                        }
                    }
                    final_phone.clear();
                    note = "";
                    ContentResolver resolver = getContentResolver();
                    Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

                    while (cursor.moveToNext()) {
                        String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND" + ContactsContract.Data.MIMETYPE + " = ?";
                        String[] noteWhereParams = new String[] {id, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                        /*Cursor noteCur = resolver.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                        while (noteCur.moveToNext()) {
                            note = noteCur.getString
                        }*/
                        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] {id}, null);
                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            if (note.equalsIgnoreCase("Emergency")) {
                                final_phone.add(phoneNumber + ", ");
                            }
                        }
                    }

                    String total_num = "";
                    for (int i = 0; i < final_phone.size(); i += 1) {
                        if (i % 2 == 0) {
                            total_num += final_phone.get(i) + "";
                        }
                    }
                    total_num = total_num.substring(0, total_num.length() - 1);
                    String message = "I need help. " + Uri.parse(location_str + temp);
                    Uri sendSMSTo = Uri.parse("smsto:" + total_num);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, sendSMSTo);
                    intent.putExtra("sms_body", message);
                    check_base = false;
                    startActivity(intent);
                } catch (SecurityException e) {
                    Log.d("sec", "Security Issue");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates("gps", 1000, 1, locationListener);
                }
                return;
        }
    }
}