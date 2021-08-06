package com.johnolafenwa.pytorchandroid;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.LocationManager;
import android.location.Location;
import android.content.Context;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
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

	// TODO: request user for location permission here

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String lat = String.format("%.5g%n", latitude);
        String lon = String.format("%.5g%n", longitude);
        String loc = lat + ", " + lon;

        TextView textView_loc = findViewById(R.id.loc);
        textView_loc.setText(loc);

	// TODO: request user for storage permission and store the resulting image
    }

}
