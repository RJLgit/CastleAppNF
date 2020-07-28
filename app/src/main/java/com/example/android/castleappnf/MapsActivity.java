package com.example.android.castleappnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int REQUEST_LOCATION_PERMISSION = 12;
    private static final int PERMISSIONS_REQUEST_ENABLE_GPS = 13;
    private GoogleMap mMap;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    ArrayList<Castles> myCastles;
    boolean locationPermissionGranted;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private static final String TAG = "MapsActivity";
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        myCastles = DummyData.generateAndReturnDataAZ(this);

        coordinatorLayout = findViewById(R.id.cordlay);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle("Map of castles");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (isMapsEnabled()) {
            mapFragment.getMapAsync(this);
        }
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        Log.d(TAG, "isMapsEnabled: ");
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    //Method builds an Alert which allows the user to enable GPS permission.
    private void buildAlertMessageNoGps() {
        Log.d(TAG, "buildAlertMessageNoGps: ");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //On activity result called when the GPS permission is granted. This then checks to see if the location permission is granted. If not it requests that. If so then it continues with the app function
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS:
                mapFragment.getMapAsync(this);
            }
        }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableMyLocation();

        populateMap();
        mMap.setOnMarkerClickListener(this);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            Log.d(TAG, "enableMyLocation: ");
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            locationPermissionGranted = true;
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            try {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Log.d(TAG, "onComplete: success");
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation == null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(53.9600,
                                                -1.0873), 8));
                                Log.d(TAG, "onComplete: last location null");
                            } else {
                                Log.d(TAG, "onComplete: location not null" + lastKnownLocation);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 8));
                            }
                        } else {
                            Log.d(TAG, "onComplete: task not success");
                            LatLng uk = new LatLng(53.9600, -1.0873);
                            float zoom = 5;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uk, zoom));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage(), e);
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this).setTitle(getString(R.string.permission_rationale_title))
                        .setMessage(getString(R.string.permission_rationale_message))
                        .setPositiveButton(getString(R.string.ok_alert_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_LOCATION_PERMISSION);

                            }
                        })
                        .setNegativeButton(getString(R.string.cancel_alert_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: general");
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: ");
                    enableMyLocation();
                    break;
                } else {
                    //Forces app to be closed if permission not granted.
                    final AlertDialog.Builder pBuilder = new AlertDialog.Builder(this);
                    pBuilder.setMessage(getString(R.string.no_location_permission_message))
                            .setCancelable(false)
                            .setNeutralButton(getString(R.string.close_app_alert_option), new DialogInterface.OnClickListener() {
                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    finishAffinity();
                                    System.exit(0);
                                }
                            });
                    final AlertDialog pAlert = pBuilder.create();
                    pAlert.show();
                }
        }
    }

    private void populateMap() {
        for (Castles c : myCastles) {
            LatLng pos = new LatLng(c.getLat(), c.getLongdi());
            mMap.addMarker(new MarkerOptions().position(pos)
            .title(c.getName()).snippet("Operated by: " + c.getOperator()));
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        marker.showInfoWindow();
        final Snackbar snackBar = Snackbar.make(coordinatorLayout, marker.getTitle(), Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction("See details", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Castles clicked = myCastles.get(0);
                for (Castles c : myCastles) {
                    if (c.getName().equals(marker.getTitle())) {
                        clicked = c;
                    }
                }

                Intent intent = new Intent(MapsActivity.this, DetailsActivity.class);
                intent.putExtra("Castle", clicked);
                startActivity(intent);
            }
        });
        snackBar.show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}