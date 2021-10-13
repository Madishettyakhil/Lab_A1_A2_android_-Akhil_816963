package com.android.androidassignment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    LocationManager locationManager;
    public static final int REQUEST_CODE = 1;

    ProductDBHelper dbHelper;

    Product product;
    int id;
    String providername;

    double latitude = 43.7803837;
    double longitude = -79.306281;

    String activity_name;

    private FusedLocationProviderClient fusedLocationProviderClient;

    Location currentlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        id = getIntent().getIntExtra("id", 1);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

        activity_name = getIntent().getStringExtra("activity");

        if (activity_name.equals("viewing")) {
            dbHelper = new ProductDBHelper(this);
            dbHelper.openDataBase();

            product = dbHelper.getProductbyID(id);

            providername = product.getProvidername();
            latitude = Double.parseDouble(product.getLatitude());
            longitude = Double.parseDouble(product.getLongitude());

            dbHelper.close();

        } else if (activity_name.equals("entry")) {
            providername = "provider location";
            latitude = getIntent().getDoubleExtra("latitude", 43.7803837);
            longitude = getIntent().getDoubleExtra("longitude", -79.306281);
        }
        getSupportActionBar().setTitle(providername);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getDeviceLocation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                assert mapFragment != null;
                mapFragment.getMapAsync(MapsActivity.this);
            }
        }, 2000);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng latlng = new LatLng(latitude, longitude);
        Drawable drawable = AppCompatResources.getDrawable(MapsActivity.this, R.drawable.ic_baseline_add_location_24);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth() + 100, drawable.getIntrinsicHeight() + 100, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        if (activity_name.equals("entry")) {
            googleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    .draggable(true));

        } else {
            Objects.requireNonNull(googleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    .title(providername).draggable(false)));

        }

        float zoomLevel = 16.0f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));
        Intent returnIntent = new Intent();
        returnIntent.putExtra("latitude", latitude);
        returnIntent.putExtra("longitude", longitude);
        setResult(Activity.RESULT_OK, returnIntent);

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("latitude", marker.getPosition().latitude);
                returnIntent.putExtra("longitude", marker.getPosition().longitude);
                setResult(Activity.RESULT_OK, returnIntent);

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("latitude", marker.getPosition().latitude);
                returnIntent.putExtra("longitude", marker.getPosition().longitude);
                setResult(Activity.RESULT_OK, returnIntent);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void getDeviceLocation() {


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
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location location = task.getResult();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    System.out.println("location : "+latitude);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}