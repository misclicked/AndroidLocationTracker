package com.misclicked.nav;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng tree = new LatLng(23.000398, 120.216153);
    private LocationManager locationManager;
    private Marker myMarker;
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

    public void centreMapOnLocation(Location location, String title, boolean first) {
        //if (location == null) return;
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (myMarker != null) {
            myMarker.remove();
        }
        myMarker = mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
        if (first) {
            CameraPosition camPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(18).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getIntent();

        if (intent.getIntExtra("Place Number", 0) == 0) {

            // Zoom into users location
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    new HttpRequest().execute("http://[2001:288:7001:270a:5d01:9f26:e46b:97a9]/upload?x=" + location.getLatitude() + "&y=" + location.getLongitude());
                    centreMapOnLocation(location, "Your Location", false);
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

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                List<String> providers = locationManager.getAllProviders();
                Location bestLocation = null;
                ;
                for (String provider : providers) {
                    Location l = locationManager.getLastKnownLocation(provider);
                    if (l == null) {
                        continue;
                    }
                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                        // Found best last known location: %s", l);
                        bestLocation = l;
                    }
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                initialLocation(lastKnownLocation == null ? bestLocation : lastKnownLocation);
                new HttpRequest().execute("http://[2001:288:7001:270a:5d01:9f26:e46b:97a9]/upload?x="
                        + (lastKnownLocation == null ? bestLocation : lastKnownLocation).getLatitude()
                        + "&y=" + (lastKnownLocation == null ? bestLocation : lastKnownLocation).getLongitude());
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                List<String> providers = locationManager.getAllProviders();
                Location bestLocation = null;
                ;
                for (String provider : providers) {
                    Location l = locationManager.getLastKnownLocation(provider);
                    if (l == null) {
                        continue;
                    }
                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                        // Found best last known location: %s", l);
                        bestLocation = l;
                    }
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                initialLocation(lastKnownLocation == null ? bestLocation : lastKnownLocation);
                new HttpRequest().execute("http://[2001:288:7001:270a:5d01:9f26:e46b:97a9]/upload?x="
                        + (lastKnownLocation == null ? bestLocation : lastKnownLocation).getLatitude()
                        + "&y=" + (lastKnownLocation == null ? bestLocation : lastKnownLocation).getLongitude());
            }
        }
    }

    private void initialLocation(Location lastKnownLocation) {
        //if (lastKnownLocation == null) return;
        centreMapOnLocation(lastKnownLocation, "Your Location", true);

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException {
                JSONObject jsonObject = new JSONObject(output);
                JSONArray routes = jsonObject.getJSONArray("routes");
                JSONObject routeObject = routes.getJSONObject(0);
                JSONObject polylinesObject = routeObject.getJSONObject("overview_polyline");
                String points = polylinesObject.getString("points");
                List<LatLng> decoded = PolyUtil.decode(points);
                mMap.addPolyline(new PolylineOptions().addAll(decoded));
            }
        };
        httpRequest.execute("https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + "&" +
                "destination=" + tree.latitude + "," + tree.longitude + "&" +
                "mode=walking&" +
                "key=" + getString(R.string.google_direction_key));
    }
}