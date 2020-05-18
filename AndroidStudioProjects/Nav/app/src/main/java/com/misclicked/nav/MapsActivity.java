package com.misclicked.nav;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng CSIE = new LatLng(22.997522, 120.221095);
    private LatLng tree = new LatLng(23.000398, 120.216153);
    private LatLng middle = new LatLng(22.998615, 120.218487);
    private LocationManager locationManager;
    private String provider;
    private int updateTime = 2500;
    private int updateDistance = 5;
    private static final int REQUEST_LOCATION = 1;
    private Marker myMarker;
    private GoogleApiClient mGoogleApiClient;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void centreMapOnLocation(Location location, String title){
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
        //mMap.clear();
        if (myMarker != null) {
            myMarker.remove();
        }
        myMarker = mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
        CameraPosition camPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mGoogleApiClient.connect();
        //mMap.addMarker(new MarkerOptions().position(CSIE).title("NCKU CSIE"));
        //mMap.addMarker(new MarkerOptions().position(tree).title("NCKU tree"));
        drawNavigationLine();
        Intent intent = getIntent();
        if (intent.getIntExtra("Place Number",0) == 0 ){

            // Zoom into users location
            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    new HttpRequest().execute("http://192.168.55.4/upload?x="+location.getLatitude()+"&y="+location.getLongitude());
                    centreMapOnLocation(location,"Your Location");
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centreMapOnLocation(lastKnownLocation,"Your Location");
            } else {

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middle, 16.0f));
    }

    private void drawNavigationLine() {
        PolylineOptions polylineOption = new PolylineOptions();
        polylineOption.add(new LatLng(22.996812, 120.220217));
        polylineOption.add(new LatLng(22.996924, 120.218954));
        polylineOption.add(new LatLng(22.998392, 120.219091));
        polylineOption.add(new LatLng(22.998483, 120.217887));
        polylineOption.add(new LatLng(22.999225, 120.217975));
        polylineOption.add(new LatLng(22.999519, 120.217957));
        polylineOption.add(new LatLng(22.999662, 120.217731));
        polylineOption.add(new LatLng(22.999909, 120.216101));
        polylineOption.add(tree);

        polylineOption.color(Color.RED);
        Polyline polyline = mMap.addPolyline(polylineOption);
        polyline.setWidth(10);
    }

    private void myPosition() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            }
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        }
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(CSIE.latitude);
        location.setLongitude(CSIE.longitude);
        updateLocation(location);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centreMapOnLocation(lastKnownLocation,"Your Location");
            }
        }
    }

    private void showMyMarker(Location location){
        if (location == null){
            return;
        }
        if (myMarker == null) {
            myMarker = mMap.addMarker(new MarkerOptions().flat(true).anchor(0.5f, 0.5f).position(new LatLng(location.getLatitude(), location.getLongitude())));
        }

        animateMarker(myMarker, location);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    private void animateMarker(final Marker marker, final Location location){
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        final double startRotation = marker.getRotation();
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = t * location.getLongitude() + (1 - t)
                        * startLatLng.longitude;
                double lat = t * location.getLatitude() + (1 - t)
                        * startLatLng.latitude;

                float rotation = (float) (t * location.getBearing() + (1 - t)
                        * startRotation);

                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation(rotation);

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void updateLocation(Location location){
        if(location!=null){
            showMyMarker(location);
        }else{
            return;
        }
    }
}
