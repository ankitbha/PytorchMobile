package com.johnolafenwa.pytorchandroid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.*;

public class Result extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOC_REQUEST_CODE = 002;

    private View resLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resLayout = findViewById(R.id.result_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bitmap imageBitmap = (Bitmap) getIntent().getBundleExtra("imagedata").get("data");

        String pred = getIntent().getStringExtra("pred");

        ImageView imageView = findViewById(R.id.image);
        imageView.setImageBitmap(imageBitmap);

        TextView textView_pred = findViewById(R.id.pred);
        textView_pred.setText(pred);

        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Calendar.getInstance().getTime());
        TextView textView_date = findViewById(R.id.date);
        textView_date.setText(date);

        // get the location info 
        checkShowLocationInfo();

        // TODO: request user for storage permission and store the resulting image
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == LOC_REQUEST_CODE) {
            // Request for location permission
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted
                showLocationInfo();
            } else {
                // Permission request was denied
                Snackbar.make(resLayout, R.string.loc_permission_denied, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void checkShowLocationInfo() {
        // TODO: request user for location permission here
        if (ActivityCompat.checkSelfPermission(Result.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // permission is already available
            showLocationInfo();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(Result.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // provide a rationale and request permission
            Snackbar.make(resLayout, R.string.loc_access_required, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // request the permission
                            requestLocationPermission();
                        }
                    }).show();
        } else {
            // permission is not available and must be requested
            Snackbar.make(resLayout, R.string.loc_access_required, Snackbar.LENGTH_SHORT).show();
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(Result.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOC_REQUEST_CODE);
    }

    private void showLocationInfo() {
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Location location = getLastKnownLocation();
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String lat = String.format("%.5g%n", latitude);
        String lon = String.format("%.5g%n", longitude);
        String loc = lat + ", " + lon;

        TextView textView_loc = findViewById(R.id.loc);
        textView_loc.setText(loc);
    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}


