package com.example.snow_scrapper;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.snow_scrapper.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    List<Address> listGeoCoder;
    public String latestMarkedAddress;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String userId;
    private static final int LOCATION_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (isLocationPermissionGranted()){
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);



            try {
                listGeoCoder   = new Geocoder(this).getFromLocationName("CN tower, Toronto",1);
            }catch (Exception e ){
                e.printStackTrace();
            }
            double lat = listGeoCoder.get(0).getLatitude();
            double log = listGeoCoder.get(0).getLatitude();

            Log.i("gooogle_map_tag", "onCreate: address has latitude::"+ String.valueOf(lat) + "latitude :: "+String.valueOf(log));

        } else {
            requestLocationPermission();
        }

        Button saveLocation = (Button) findViewById(R.id.saveLocation);
        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                userId = mAuth.getCurrentUser().getUid();
                DocumentReference documentReference =
                        db.getInstance().collection("users").document(userId);
                Map<String, Object> map = new HashMap<>();
                map.put("address",latestMarkedAddress);
                documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ",e);
                    }
                })
                ;



            }
        });

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

        if  (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            //LatLng latLng = new LatLng(latitude, longitude);
            //LatLng myPosition = new LatLng(latitude, longitude);


            LatLng coordinate = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 19);
            mMap.animateCamera(yourLocation);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                Geocoder geocoder =
                        new Geocoder(MapsActivity.this);
                List<Address> list;
                try {
                    list = geocoder.getFromLocation(latLng.latitude,
                            latLng.longitude, 1);
                    latestMarkedAddress = list.get(0).getAddressLine(0);
                    //latestMarkedAddress.getAddressLine(0);
                    Toast.makeText(getApplicationContext(), latestMarkedAddress.toString() , Toast.LENGTH_LONG)
                            .show();
                } catch (IOException e) {
                    return;
                }
                Address address = list.get(0);

//                if (marker != null) {
//                    marker.remove();
//                }

                MarkerOptions options = new MarkerOptions()
                        .title(address.getAddressLine(0))
                        .position(new LatLng(latLng.latitude,
                                latLng.longitude));
                mMap.clear();
                mMap.addMarker(options);
            }
        });
    }

    private boolean isLocationPermissionGranted(){
        if  (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED){
            return true;

        } else  {
            return false;
        }
    }
    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_CODE );
    }
}