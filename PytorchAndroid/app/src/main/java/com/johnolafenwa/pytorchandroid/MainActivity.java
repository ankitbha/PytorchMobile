package com.johnolafenwa.pytorchandroid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.Manifest;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import java.io.File;

public class MainActivity extends AppCompatActivity 
    implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int CAMERA_REQUEST_CODE = 001;

    private View mLayout;

    private Regressor regressor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	mLayout = findViewById(R.id.main_layout);
	
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        regressor = new Regressor(Utils.assetFilePath(this,"mobile_model.pt"));

        Button capture = findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener(){

		@Override
		public void onClick(View view){
		    
		    // TODO: check for camera permissions here before
		    // launching the camera intent
		    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) 
			== PackageManager.PERMISSION_GRANTED) {
			// permission is already available, so we can start the camera
			Snackbar.make(mLayout, R.string.camera_permission_available, Snackbar.LENGTH_SHORT).show();
			startCamera();
		    } else {
			// permission is not available and must be requested
			if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
			    
			    // provide a rationale and request permission
			    Snackbar.make(mLayout, R.string.camera_access_required,
					  Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
						  @Override
						  public void onClick(View view) {
						      // request the permission
						      ActivityCompat.requestPermissions(MainActivity.this, 
											new String[] {Manifest.permission.CAMERA},
											CAMERA_REQUEST_CODE);
						  }
					      }).show();
			} else {
			    Snackbar.make(mLayout, R.string.camera_unavailable, Snackbar.LENGTH_SHORT).show();
			    
			    // request permission
			    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
			}
		    }
		    
		}
	    });
	
    }
    
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CAMERA_REQUEST_CODE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, R.string.camera_permission_granted, Snackbar.LENGTH_SHORT).show();
                startCamera();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, R.string.camera_permission_denied, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
	Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            Intent resultView = new Intent(this,Result.class);

            resultView.putExtra("imagedata",data.getExtras());

            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

            String pred = regressor.predict(imageBitmap);
            resultView.putExtra("pred",pred);

            startActivity(resultView);

        }

    }

}
