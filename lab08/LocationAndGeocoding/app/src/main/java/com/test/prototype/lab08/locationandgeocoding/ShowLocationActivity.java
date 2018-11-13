package com.test.prototype.lab08.locationandgeocoding;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Locale;

public class ShowLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;
    private static LocationManager locationManager;
    private static int updateTime = 1000;
    private static int updateDistance = 2;
    private static final float MIN_ZOOM = 14.0f;
    private static final float MAX_ZOOM = 18.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        requestLocationPermission();

        // Check if user has GPS disabled
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        checkLocationEnabled();
    }

    private void requestLocationPermission() {
        // Check if app has been granted location permissions, and request it if not granted
        if (!(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void checkLocationEnabled() {
        // Check if Location (GPS) is enabled
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Show popup to take user to settings to enable location services
            AlertDialog.Builder alert = new AlertDialog.Builder(ShowLocationActivity.this);
            alert.setTitle("Location Required");
            alert.setMessage("GPS and location access is needed. Go to Settings to enable it now?");
            alert.setNegativeButton("No", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String locationConfig = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                    Intent enableLocationIntent = new Intent(locationConfig);
                    startActivity(enableLocationIntent);
                }
            });
            alert.show();
        }
    }

    private String getRecommendedLocationProvider() {

        // Set location provider criteria
        Criteria locationProviderCriteria = new Criteria();
        locationProviderCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationProviderCriteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        locationProviderCriteria.setAltitudeRequired(false);
        locationProviderCriteria.setBearingRequired(false);
        locationProviderCriteria.setSpeedRequired(false);
        locationProviderCriteria.setCostAllowed(false);

        return locationManager.getBestProvider(locationProviderCriteria, true);
    }

    private void setLocation(Location location) {

        LatLng currLocation = new LatLng(location.getLatitude(), location.getLongitude());

        // Move the camera to current location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, MAX_ZOOM));

        // Send address to fragment
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Address addr = (geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1)).get(0);
                ((ShowAddressFragment)getSupportFragmentManager().findFragmentById(R.id.address)).setAddress(addr);
            } catch (IOException e) {
                System.out.println("IOError occurred while reading address");
            } catch (Exception e2) {
                System.out.println("Another error occurred while reading address");
                e2.printStackTrace();
            }

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

        // Map settings
        mMap = googleMap;
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(false);
        mMap.setMaxZoomPreference(MAX_ZOOM);
        mMap.setMinZoomPreference(MIN_ZOOM);
        mMap.setTrafficEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        // Listen for changes to location
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                requestLocationPermission();
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                checkLocationEnabled();
            }
        };

        // Set location and listen for changes
        String locationProvider = getRecommendedLocationProvider();
        try {
            // Enable current location and accuracy circle
            mMap.setMyLocationEnabled(true);
            setLocation(locationManager.getLastKnownLocation(locationProvider));
            locationManager.requestLocationUpdates(locationProvider, updateTime, updateDistance, locationListener);
        } catch (SecurityException se) {
            System.out.println("Security exception occurred");
        } catch (Exception e) {
            System.out.println("Another error occurred");
            e.printStackTrace();
        }
    }

}
