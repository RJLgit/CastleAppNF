package com.example.android.castleappnf;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity implements CastleAdapter.OnRecyclerItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    //Constant for the permission request for GPS and location
    private static final int PERMISSIONS_REQUEST_ENABLE_GPS = 5;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 8;
    private static final String TAG = "MainActivity";

    //UI elements
    Toolbar toolbar;
    RecyclerView recyclerView;
    CastleAdapter castleAdapter;
    //UI elements shown when connection status changes
    TextView bottStatus;
    BottomNavigationView bottNav;

    //Firebase and storage variables
    private StorageReference mStorageRef;

    //Variables that help to obtain and store phone location
    private boolean mLocationPermissionGranted = false;
    private Location l;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;

    //Shared preference variables
    SharedPreferences sharedPreferences;
    String distanceUnit;
    String sortBy;
    String filterBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Assign UI elements to variables
        recyclerView = findViewById(R.id.recyclerView);
        bottStatus = findViewById(R.id.bottom_main_status_text_view);
        bottNav = findViewById(R.id.bott_nav_bar);
        Log.d(TAG, "onCreate: ");

        createWorkerNotification();
        //Gets storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        setUpSharedPreferences();
        if (isMapsEnabled()) {
            getLocationPermission();
        }
    }

    //Creates peroidic work request object and uses workmanager to send notification every 48 hours
    private void createWorkerNotification() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();
        PeriodicWorkRequest workRequest =
                new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.SECONDS)
                        //.setInitialDelay(1, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(NotificationWorker.NOT_TASK_ID,
                ExistingPeriodicWorkPolicy.KEEP, workRequest);
    }



    //Triggered when phone connects to the internet by broadcast receiver. Means when phone connects it is able to gain access to firebase
    @Override
    public void logIn(boolean b) {
        if (b) {

        } else {
            mStorageRef = FirebaseStorage.getInstance().getReference();

            setUpSharedPreferences();
            if (isMapsEnabled()) {
                getLocationPermission();
                            }
        }
    }


    //Sets up the shared preferences file and finds the current shared preference values for each of the preferences
    private void setUpSharedPreferences() {
        Log.d(TAG, "setUpSharedPreferences: ");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        distanceUnit = sharedPreferences.getString(getString(R.string.shared_preference_distance_key), getString(R.string.shared_preference_distance_default));
        sortBy = sharedPreferences.getString(getString(R.string.shared_preference_sort_key), getString(R.string.shared_preference_sort_default));
        filterBy = sharedPreferences.getString(getString(R.string.shared_preference_filter_key), getString(R.string.shared_preference_filter_default));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    //Method which checks whether GPS is enabled on the device. If it is then it returns true. Otherwise it returns false and builds an alert to requet GPS enabled
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

    //Checks if location permission granted. If so it sets the boolean variable to true for future reference.
    //If not then it requests the permission from the user. If the user previously rejected the permission then permission rationale is shown
    //If user not previously rejected permission then it is requested.
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        Log.d(TAG, "getLocationPermission: ");
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            permissionsAndGpsGranted();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this).setTitle("Permission needed")
                        .setMessage("This permission is needed for the app to run")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }


    private void permissionsAndGpsGranted() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        Log.d(TAG, "permissionsAndGpsGranted: ");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("UK Castles");
        toolbar.setSubtitle("Click Castle to see more info");
        setSupportActionBar(toolbar);
        final Context context = this;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        castleAdapter = new CastleAdapter(getApplicationContext(), DummyData.generateAndReturnDataAZ(getApplicationContext()), (CastleAdapter.OnRecyclerItemClickListener) context, distanceUnit, sortBy, mStorageRef, filterBy);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            l = new Location(location);
                            castleAdapter.setPhoneLocation(l);

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
                    if (location != l) {
                        l = location;
                        castleAdapter.setPhoneLocation(l);
                        //castleAdapter.notifyItemRangeChanged(0, castleAdapter.getItemCount(), l);
                        recyclerView.setAdapter(castleAdapter);
                        Log.d(TAG, "onLocationResult: " + location);
                        Log.d(TAG, "onLocationResult: " + castleAdapter);
                    }

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
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        if (mLocationPermissionGranted) {
            startLocationUpdates();
        }


    }


    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
        Log.d(TAG, "startLocationUpdates: ");
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setFastestInterval(60000);
        locationRequest.setSmallestDisplacement(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Log.d(TAG, "createLocationRequest: ");
    }







 /*       if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            permissionsAndGpsGranted();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }*/

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

                    Log.d(TAG, "onRequestPermissionsResult: granted");
                    mLocationPermissionGranted = true;
                    permissionsAndGpsGranted();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: failed");
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
            //Add slide out top/bottom animations
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("distance_preference")) {
            distanceUnit = sharedPreferences.getString("distance_preference", "Miles");
            createAndSetAdapter();
        }
        if (s.equals("sort_preference")) {
            sortBy = sharedPreferences.getString("sort_preference", "A-Z");
            if (sortBy.equals("Distance")) {
                createAndSetAdapter();
            } else if (sortBy.equals("A-Z")) {
                createAndSetAdapter();
            } else if (sortBy.equals("Rating")) {
                createAndSetAdapter();
            }
        }
        if (s.equals("filter_preference")) {
            filterBy = sharedPreferences.getString("filter_preference", "None");
            if (filterBy.equals("English Heritage")) {
                Log.d(TAG, "onSharedPreferenceChanged: " + filterBy);
                createAndSetAdapter();
            }
            if (filterBy.equals("None")) {
                Log.d(TAG, "onSharedPreferenceChanged: " + filterBy);
                createAndSetAdapter();
            }
        }
    }

    private void createAndSetAdapter() {
        castleAdapter = new CastleAdapter(getApplicationContext(), DummyData.generateAndReturnDataAZ(getApplicationContext()), this, distanceUnit, sortBy, mStorageRef, filterBy);
        castleAdapter.setPhoneLocation(l);
        recyclerView.setAdapter(castleAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
        /*IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, filter);*/
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
        //unregisterReceiver(connectionReceiver);
    }

    @Override
    public void showBottomToolBar() {
        Log.d(TAG, "showBottomToolBar: ");
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 60);
        bottNav.setVisibility(View.VISIBLE);
        bottStatus.setText("Offline");
        bottStatus.setBackgroundColor(Color.RED);
    }

    @Override
    public void hideBottomToolBar() {
        Log.d(TAG, "hideBottomToolBar: ");
        if (bottNav.getVisibility() == View.VISIBLE) {
            bottStatus.setText("Online");
            bottStatus.setBackgroundColor(Color.GREEN);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
                    layoutParams.setMargins(0, 0, 0, 0);
                    bottNav.setVisibility(View.INVISIBLE);
                }
            }, 5000);
        }


    }
}
