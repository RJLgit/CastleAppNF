package com.example.android.castleappnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends BaseActivity implements CastleAdapter.OnRecyclerItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

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
    String sortBy;
    RecyclerView recyclerView;
    private StorageReference mStorageRef;
    TextView bottStatus;
    BottomNavigationView bottNav;
    Uri image;
    private FirebaseAuth mAuth;
    //ConnectionReceiver connectionReceiver = new ConnectionReceiver();
    CastleAdapter castleAdapter;

    private FusedLocationProviderClient fusedLocationClient;

    public void logIn() {

        Log.d(TAG, "logIn: ");
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInAnonymously:success");
                        Toast.makeText(MainActivity.this, "Authentication Success.",
                                Toast.LENGTH_SHORT).show();
                        mStorageRef = FirebaseStorage.getInstance().getReference();
//        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    image = uri;
//                                    Log.d(TAG, "onSuccess: " + uri);
//                                    setUpSharedPreferences();
//                                    if (isMapsEnabled()) {
//
//                                        getLocationPermission();
//                                    }
//                                }
//                            });


                        setUpSharedPreferences();
                        if (isMapsEnabled()) {

                            getLocationPermission();
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }

                    // ...
                }
            });
        } else {
            mStorageRef = FirebaseStorage.getInstance().getReference();
//        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    image = uri;
//                                    Log.d(TAG, "onSuccess: " + uri);
//                                    setUpSharedPreferences();
//                                    if (isMapsEnabled()) {
//
//                                        getLocationPermission();
//                                    }
//                                }
//                            });


            setUpSharedPreferences();
            if (isMapsEnabled()) {

                getLocationPermission();
            }
        }
    }

    @Override
    public void logIn(boolean b) {
        if (b) {

        } else {
            mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() == null) {
                mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            Toast.makeText(MainActivity.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            mStorageRef = FirebaseStorage.getInstance().getReference();
//        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    image = uri;
//                                    Log.d(TAG, "onSuccess: " + uri);
//                                    setUpSharedPreferences();
//                                    if (isMapsEnabled()) {
//
//                                        getLocationPermission();
//                                    }
//                                }
//                            });


                            setUpSharedPreferences();
                            if (isMapsEnabled()) {

                                getLocationPermission();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
            } else {
                mStorageRef = FirebaseStorage.getInstance().getReference();
//        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    image = uri;
//                                    Log.d(TAG, "onSuccess: " + uri);
//                                    setUpSharedPreferences();
//                                    if (isMapsEnabled()) {
//
//                                        getLocationPermission();
//                                    }
//                                }
//                            });


                setUpSharedPreferences();
                if (isMapsEnabled()) {

                    getLocationPermission();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        bottStatus = findViewById(R.id.bottom_main_status_text_view);
        bottNav = findViewById(R.id.bott_nav_bar);
        Log.d(TAG, "onCreate: ");

        logIn();








    }

    private void setUpSharedPreferences() {
        Log.d(TAG, "setUpSharedPreferences: ");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        distanceUnit = sharedPreferences.getString("distance_preference", "Miles");
        sortBy = sharedPreferences.getString("sort_preference", "A-Z");
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
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
        castleAdapter = new CastleAdapter(getApplicationContext(), DummyData.generateAndReturnDataAZ(getApplicationContext()), (CastleAdapter.OnRecyclerItemClickListener) context, distanceUnit, sortBy, mStorageRef);
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
        locationRequest.setSmallestDisplacement(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Log.d(TAG, "createLocationRequest: ");
    }

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
            /*case ConnectionReceiver.REQUEST_CONN: {
                boolean noConnectivity = data.getBooleanExtra(
                        ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
                );
                if (!noConnectivity) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                }
            }*/
        }

    }

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
            CastleAdapter castleAdapter = new CastleAdapter(getApplicationContext(), DummyData.generateAndReturnDataAZ(getApplicationContext()), this, distanceUnit, sortBy, mStorageRef);
            castleAdapter.setPhoneLocation(l);
            recyclerView.setAdapter(castleAdapter);
        }
        if (s.equals("sort_preference")) {
            sortBy = sharedPreferences.getString("sort_preference", "A-Z");
            if (sortBy.equals("Distance")) {
                CastleAdapter castleAdapter = new CastleAdapter(getApplicationContext(), DummyData.generateAndReturnDataAZ(getApplicationContext()), this, distanceUnit, sortBy, mStorageRef);
                castleAdapter.setPhoneLocation(l);
                recyclerView.setAdapter(castleAdapter);
            } else if (sortBy.equals("A-Z")) {
                CastleAdapter castleAdapter = new CastleAdapter(getApplicationContext(), DummyData.generateAndReturnDataAZ(getApplicationContext()), this, distanceUnit, sortBy, mStorageRef);
                castleAdapter.setPhoneLocation(l);
                recyclerView.setAdapter(castleAdapter);
            } else if (sortBy.equals("Rating")) {
                CastleAdapter castleAdapter = new CastleAdapter(getApplicationContext(), DummyData.generateAndReturnDataAZ(getApplicationContext()), this, distanceUnit, sortBy, mStorageRef);
                castleAdapter.setPhoneLocation(l);
                recyclerView.setAdapter(castleAdapter);
            }
        }
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
