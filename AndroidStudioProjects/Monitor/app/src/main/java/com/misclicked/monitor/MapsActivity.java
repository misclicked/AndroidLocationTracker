package com.misclicked.monitor;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.Timer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AsyncResponse {

    private GoogleMap mMap;
    private Marker myMarker;
    Timer timer;
    TrackerTask trackerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        timer = new Timer();
        trackerTask = new TrackerTask();
        trackerTask.delegate = this;
        timer.schedule(trackerTask, 0, 500);
    }

    public void centreMapOnLocation(LatLng location, String title, boolean first) {
        if (myMarker != null) {
            myMarker.remove();
        }
        myMarker = mMap.addMarker(new MarkerOptions().position(location).title(title));
        if (first) {
            CameraPosition camPosition = new CameraPosition.Builder().target(new LatLng(location.latitude, location.longitude)).zoom(18).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));
        }
    }

    private LatLng lastSeenLoc = null;

    @Override
    public void processFinish(String output) {
        double[] parsed = parseResponse(output);
        LatLng currentLoc = new LatLng(parsed[0], parsed[1]);
        if (lastSeenLoc == null || lastSeenLoc != currentLoc) {
            centreMapOnLocation(currentLoc, "Current location", lastSeenLoc == null);
            lastSeenLoc = currentLoc;
        }
    }

    private double[] parseResponse(String response) {
        return new double[]{Double.parseDouble(response.split(",")[0]), Double.parseDouble(response.split(",")[1])};
    }
}
