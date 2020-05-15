package com.example.android.castleappnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements CastleAdapter.OnRecyclerItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int PERMISSIONS_REQUEST_ENABLE_GPS = 5;
    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 8;
    Toolbar toolbar;
    private boolean mLocationPermissionGranted = false;
    private Location l;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    SharedPreferences sharedPreferences;
    String distanceUnit;
    RecyclerView recyclerView;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpSharedPreferences();
        if (isMapsEnabled()) {
            
            getLocationPermission();
        }


    }

    private void setUpSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        distanceUnit = sharedPreferences.getString("distance_preference", "Miles");
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }


    private void permissionsAndGpsGranted() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("UK Castles");
        toolbar.setSubtitle("Click Castle to see more info");
        setSupportActionBar(toolbar);
        final Context context = this;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            l = new Location(location);
                            CastleAdapter castleAdapter = new CastleAdapter(getApplicationContext(), DummyData.generateAndReturnData(getApplicationContext()), (CastleAdapter.OnRecyclerItemClickListener) context, l, distanceUnit);
                            recyclerView.setAdapter(castleAdapter);
                            Log.d(TAG, "onSuccess: " + location);
                        }
                        Log.d(TAG, "onSuccess: " + location);
                    }
                });
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d(TAG, "onLocationResult: ");
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    l = location;
                    CastleAdapter castleAdapter = new CastleAdapter(getApplicationContext(), DummyData.generateAndReturnData(getApplicationContext()), (CastleAdapter.OnRecyclerItemClickListener) context, l, distanceUnit);
                    recyclerView.setAdapter(castleAdapter);
                    Log.d(TAG, "onLocationResult: " + location);
                }
            }
        };
        createLocationRequest();


    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();

    }


    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
        Log.d(TAG, "startLocationUpdates: ");
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5);
        locationRequest.setFastestInterval(5);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Log.d(TAG, "createLocationRequest: ");
    }

    private void buildAlertMessageNoGps() {
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

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        Log.d(TAG, "isMapsEnabled: ");
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Log.d(TAG, "isMapsEnabled: false");
            buildAlertMessageNoGps();
            
            return false;
        }
        Log.d(TAG, "isMapsEnabled: true");
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted){
                    permissionsAndGpsGranted();
                }
                else{
                    getLocationPermission();
                }
            }
        }

    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            permissionsAndGpsGranted();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    permissionsAndGpsGranted();
                } else {
                    final AlertDialog.Builder pBuilder = new AlertDialog.Builder(this);
                    pBuilder.setMessage("Please enable location permissions to use this app")
                            .setCancelable(false)
                            .setNeutralButton("Close App", new DialogInterface.OnClickListener() {
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
    }

    @Override
    public void onMyItemClicked(Castles c) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("Castle", c);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.my_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("distance_preference")) {
            distanceUnit = sharedPreferences.getString("distance_preference", "Miles");
            CastleAdapter castleAdapter = new CastleAdapter(getApplicationContext(), DummyData.generateAndReturnData(getApplicationContext()), this, l, distanceUnit);
            recyclerView.setAdapter(castleAdapter);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
