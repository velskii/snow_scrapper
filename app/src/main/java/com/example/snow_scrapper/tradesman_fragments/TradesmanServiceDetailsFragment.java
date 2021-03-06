package com.example.snow_scrapper.tradesman_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.snow_scrapper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TradesmanServiceDetailsFragment extends Fragment {

    private static final String TAG = "TradesmanServiceDetailsFragment";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String id;

    public TradesmanServiceDetailsFragment(String id) {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        getServiceDetailsById(mAuth.getUid(), this.id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tradesman_service_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnPurchase = getActivity().findViewById(R.id.tradesman_back);
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment;
                fragment = new TradesmanServiceListFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName())
                        .commit();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName());
                transaction.commit();
//
//                BottomNavigationView bnv = getActivity().findViewById(R.id.tradesman_bottom_navigation);
//
//                bnv.setSelectedItemId(R.id.tradesman_service_list);
            }
        });

    }

    public void getServiceDetailsById(String uid, String item_id) {

        db.collection("service_list")
                .whereEqualTo(FieldPath.documentId(), item_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TextView item_name = getActivity().findViewById(R.id.tradesman_item_name);
                                item_name.setText(document.getString("name"));

                                TextView location = getActivity().findViewById(R.id.tradesman_item_location);
                                location.setText(document.getString("location"));

                                ImageView image = getActivity().findViewById(R.id.tradesman_item_image);
                                image.setImageResource(R.drawable.snow1);

                                TextView price = getActivity().findViewById(R.id.tradesman_item_price);
                                price.setText(document.getString("price") + " $/square");

                                TextView rating = getActivity().findViewById(R.id.tradesman_item_rating);
                                rating.setText(document.getLong("rating").toString() + " stars");

                                TextView range = getActivity().findViewById(R.id.tradesman_item_range);
                                range.setText(document.getString("range"));

//                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

}