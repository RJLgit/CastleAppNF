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

import java.util.Set;
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
    public void userConnected(boolean b) {
        Log.d(TAG, "userConnected: " + b);
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

    //Method which responds to the result of permission requests
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        //Sets the boolean variable to false
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
                    //Forces app to be closed if permission not granted.
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

    //When all permissions have been granted, this method is launched
    private void permissionsAndGpsGranted() {
        //Set up of the recyclerview
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        Log.d(TAG, "permissionsAndGpsGranted: ");
        //Set up of the toolbar for the activity
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("UK Castles");
        toolbar.setSubtitle("Click Castle to see more info");
        setSupportActionBar(toolbar);

        final Context context = this;
        //Fused location client is created
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //Adapter is created with dummy data, firebase reference and shared preference information
        castleAdapter = new CastleAdapter(getApplicationContext(), DummyData.generateAndReturnDataAZ(getApplicationContext()), (CastleAdapter.OnRecyclerItemClickListener) context, distanceUnit, sortBy, mStorageRef, filterBy);
        //Get last location finds the last location of the phone and sets it to the adapter object. Adapter is then set to the recyclerview
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
                        } else {
                            Log.d(TAG, "onSuccess: was null" + location);

                        }

                    }
                });
        //Location callback is initiated. This object handles the location callback when the location of the phone is communicated with the app via the location request created at the end of this method
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
                        recyclerView.setAdapter(castleAdapter);
                        Log.d(TAG, "onLocationResult: " + location);
                        Log.d(TAG, "onLocationResult: " + castleAdapter);
                    }

                }
            }
        };
        createLocationRequest();
    }

    //Creates a location request to regularly get the location for the app.
    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        //Location communicated every 60 seconds but only if distance moves a certain amount
        locationRequest.setInterval(60000);
        locationRequest.setFastestInterval(60000);
        //Means that location is only communicated with the app when it changes by at least 1 KM
        locationRequest.setSmallestDisplacement(1000);
        //Accurated location obtained.
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Log.d(TAG, "createLocationRequest: ");
    }

    //If the app has permissions then location updates are started in onresume.
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        if (mLocationPermissionGranted) {
            startLocationUpdates();
        }
    }

    //Location updates are stopped in onpause
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    //The fusedlocationclient pairs together the location request that defines how accurate and often the location request is, with the location callback which defines what to do with the location request
    //The callback essentially takes the new location and uses it to change the adapter to have the new data in the recyclerview
    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
        Log.d(TAG, "startLocationUpdates: ");
    }

    //If the location client and callback exists then their association is removed to stop the app updating its location in the background.
    private void stopLocationUpdates() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


    //Creates the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    //When an option is selected from the settings menu then it starts the settings activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.my_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            //Adds in animations to move between activities
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //This method is called when the shared preferences are changed. Each time they are changed then it recreates the adapter and sets it to the recyclerview with different sharedpreference variables.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.shared_preference_distance_key))) {
            distanceUnit = sharedPreferences.getString(getString(R.string.shared_preference_distance_key), getString(R.string.shared_preference_distance_default));
            createAndSetAdapter();
        }
        if (s.equals(getString(R.string.shared_preference_sort_key))) {
            sortBy = sharedPreferences.getString(getString(R.string.shared_preference_sort_key), getString(R.string.shared_preference_sort_default));
            createAndSetAdapter();
        }
        if (s.equals(getString(R.string.shared_preference_filter_key))) {
            filterBy = sharedPreferences.getString(getString(R.string.shared_preference_filter_key), getString(R.string.shared_preference_filter_default));
            createAndSetAdapter();
        }
    }

    //Helper method to deal with creating the setting adapter to recyclerview
    private void createAndSetAdapter() {
        castleAdapter = new CastleAdapter(getApplicationContext(), DummyData.generateAndReturnDataAZ(getApplicationContext()), this, distanceUnit, sortBy, mStorageRef, filterBy);
        castleAdapter.setPhoneLocation(l);
        recyclerView.setAdapter(castleAdapter);
    }

    //Unregister on shared preference listener that is registered in oncreate
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    //The BaseActivity super class registers ConnectionReceiver here
    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    //The BaseActivity super class unregisters ConnectionReceiver here
    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    //Shows bottom toolbar when user goes offline
    @Override
    public void showBottomToolBar() {
        Log.d(TAG, "showBottomToolBar: ");
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 60);
        bottNav.setVisibility(View.VISIBLE);
        bottStatus.setText(R.string.offline_string);
        bottStatus.setBackgroundColor(Color.RED);
    }

    //Hides bottom toolbar when user goes online. Short delay where it shows user online using a handler postDelayed method
    @Override
    public void hideBottomToolBar() {
        Log.d(TAG, "hideBottomToolBar: ");
        if (bottNav.getVisibility() == View.VISIBLE) {
            bottStatus.setText(R.string.online_string);
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

    //Interface method that acts on onclick events in the recyclerview
    @Override
    public void onMyItemClicked(Castles c) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("Castle", c);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
