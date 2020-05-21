package com.misclicked.nav;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SharedPreferences.OnSharedPreferenceChangeListener,
        LocationResponse {

    private GoogleMap mMap;
    private LatLng tree = new LatLng(23.000398, 120.216153);
    private Marker myMarker;
    private LocationManager locationManager;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mService.requestLocationUpdates();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        myReceiver.delegate = this;
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    public void centreMapOnLocation(Location location, String title, boolean first) {
        first = true;
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (myMarker != null)
            myMarker.remove();
        myMarker = mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
        if (first) {
            CameraPosition camPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(18).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getIntent();

        if (intent.getIntExtra("Place Number", 0) == 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                initialLocation(lastKnownLocation == null ? bestLocation : lastKnownLocation);
                new HttpRequest().execute("http://" + getString(R.string.server_ip_v6) + "/upload?x="
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
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                List<String> providers = locationManager.getAllProviders();
                Location bestLocation = null;
                ;
                for (String provider : providers) {
                    Location l = locationManager.getLastKnownLocation(provider);
                    if (l == null)
                        continue;
                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy())
                        bestLocation = l;
                }
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                initialLocation(lastKnownLocation == null ? bestLocation : lastKnownLocation);
                new HttpRequest().execute("http://" + getString(R.string.server_ip_v6) + "/upload?x="
                        + (lastKnownLocation == null ? bestLocation : lastKnownLocation).getLatitude()
                        + "&y=" + (lastKnownLocation == null ? bestLocation : lastKnownLocation).getLongitude());
                mService.requestLocationUpdates();
            }
        }
    }

    private void initialLocation(Location lastKnownLocation) {
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
                "mode=driving&" +
                "key=" + getString(R.string.google_direction_key));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
    }

    @Override
    public void processFinish(Location location) {
        centreMapOnLocation(location, "Your Location", false);
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {

        public LocationResponse delegate = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                delegate.processFinish(location);
            }
        }
    }
}