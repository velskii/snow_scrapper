package com.example.snow_scrapper.tradesman_fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.snow_scrapper.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TradesmanHomeFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private static final String TAG = "TradesmanHomeFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tradesman_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn = getActivity().findViewById(R.id.add_service);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit_service_name = getActivity().findViewById(R.id.service_name);
                String service_name = edit_service_name.getText().toString();

                EditText edit_service_price = getActivity().findViewById(R.id.service_price);
                String service_price = edit_service_price.getText().toString();

                EditText edit_service_image = getActivity().findViewById(R.id.service_image);
                String service_image = edit_service_image.getText().toString();

                EditText edit_service_location = getActivity().findViewById(R.id.service_location);
                String service_location = edit_service_location.getText().toString();

                EditText edit_service_range = getActivity().findViewById(R.id.service_range);
                String service_range = edit_service_range.getText().toString();

                if (service_name.isEmpty()) {
                    edit_service_name.setError("Please input service name");
                } else if (service_price.isEmpty()) {
                    edit_service_price.setError("Please input service price");
                } else if (service_image.isEmpty()) {
                    edit_service_image.setError("Please upload service image");
                } else if (service_location.isEmpty()) {
                    edit_service_location.setError("Please input service location");
                } else if (service_range.isEmpty()) {
                    edit_service_range.setError("Please input service range");
                } else {
                    storeServiceData(service_name, service_price, service_image, service_location, service_range);
                }

            }
        });

    }


    public void storeServiceData(String service_name, String service_price, String service_image, String service_location, String service_range) {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = "";
        if(currentUser != null){
            uid = currentUser.getUid();
        }
        Map<String, Object> service = new HashMap<>();
        service.put("name", service_name);
        service.put("price", service_price);
        service.put("image", service_image);
        service.put("location", service_location);
        service.put("range", service_range);

        service.put("publisher", uid);

        // Add a new document with a generated ID
        db.collection("service_list")
                .add(service)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        updateUI();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        // [END add_ada_lovelace]
    }

    private void updateUI() {
        Fragment fragment = new TradesmanServiceListFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName())
                .commit();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName());
        transaction.commit();

        BottomNavigationView bnv = getActivity().findViewById(R.id.tradesman_bottom_navigation);

        bnv.setSelectedItemId(R.id.tradesman_service_list);
    }
}