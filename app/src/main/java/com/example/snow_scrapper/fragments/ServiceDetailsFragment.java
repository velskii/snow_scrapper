package com.example.snow_scrapper.fragments;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ServiceDetailsFragment extends Fragment {

    private static final String TAG = "ServiceDetailsFragment";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String item_id;

    public ServiceDetailsFragment(String id) {
        this.item_id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getServiceDetailsById(mAuth.getUid(), this.item_id);

        Button btnPurchase = getActivity().findViewById(R.id.customer_purchase_service);
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToOrder();
            }
        });

        Button btnCart = getActivity().findViewById(R.id.customer_add_cart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }

    public void addToCart(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = "";
        if(currentUser != null){
            uid = currentUser.getUid();
        }

        Map<String, Object> cart = new HashMap<>();
        TextView item_name = getActivity().findViewById(R.id.customer_item_name);
        cart.put( "name", item_name.getText().toString() );

        TextView location = getActivity().findViewById(R.id.customer_item_location);
        cart.put( "location", location.getText().toString() );

        TextView seller = getActivity().findViewById(R.id.customer_item_seller);
        cart.put( "seller", seller.getText().toString() );

        ImageView image = getActivity().findViewById(R.id.customer_item_image);
        cart.put( "image", image.getDrawable().toString() );

        TextView rating = getActivity().findViewById(R.id.customer_item_rating);
        cart.put( "rating", rating.getText().toString() );

        TextView range = getActivity().findViewById(R.id.customer_item_range);
        cart.put( "range", range.getText().toString() );

        TextView item_price = getActivity().findViewById(R.id.customer_item_price);
        cart.put( "price", item_price.getText().toString() );

        cart.put("service_id", this.item_id);
        cart.put("uid", uid);

        cart.put("created_time", Calendar.getInstance().getTime());
        cart.put("updated_time", Calendar.getInstance().getTime());

        // Add a new document with a generated ID
        db.collection("cart")
                .add(cart)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Fragment fragment;
                        fragment = new CartFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName())
                                .commit();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName());
                        transaction.commit();

                        BottomNavigationView bnv = getActivity().findViewById(R.id.bottom_navigation);
                        bnv.setSelectedItemId(R.id.cart);
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void addToOrder(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = "";
        if(currentUser != null){
            uid = currentUser.getUid();
        }

        Map<String, Object> orderDetail = new HashMap<>();
        TextView item_name = getActivity().findViewById(R.id.customer_item_name);
        orderDetail.put( "name", item_name.getText().toString() );

        TextView location = getActivity().findViewById(R.id.customer_item_location);
        orderDetail.put( "location", location.getText().toString() );

        TextView seller = getActivity().findViewById(R.id.customer_item_seller);
        orderDetail.put( "seller", seller.getText().toString() );

        ImageView image = getActivity().findViewById(R.id.customer_item_image);
        orderDetail.put( "image", image.getDrawable().toString() );

        TextView rating = getActivity().findViewById(R.id.customer_item_rating);
        orderDetail.put( "rating", rating.getText().toString() );

        TextView range = getActivity().findViewById(R.id.customer_item_range);
        orderDetail.put( "range", range.getText().toString() );

        TextView item_price = getActivity().findViewById(R.id.customer_item_price);
        orderDetail.put( "price", item_price.getText().toString() );

        orderDetail.put("service_id", this.item_id);
        orderDetail.put("uid", uid);

        orderDetail.put("created_time", Calendar.getInstance().getTime());
        orderDetail.put("updated_time", Calendar.getInstance().getTime());

        // Add a new document with a generated ID
        db.collection("orders")
                .add(orderDetail)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Fragment fragment;
                        fragment = new OrdersFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName())
                                .commit();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName());
                        transaction.commit();

                        BottomNavigationView bnv = getActivity().findViewById(R.id.bottom_navigation);
                        bnv.setSelectedItemId(R.id.orders);
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_details, container, false);
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
                                TextView item_name = getActivity().findViewById(R.id.customer_item_name);
                                item_name.setText(document.getString("name"));

                                TextView location = getActivity().findViewById(R.id.customer_item_location);
                                location.setText(document.getString("location"));

                                TextView seller = getActivity().findViewById(R.id.customer_item_seller);
                                seller.setText(document.getString("publisher"));

                                ImageView image = getActivity().findViewById(R.id.customer_item_image);
                                image.setImageResource(R.drawable.snow1);
                                // document.getString("image")

                                TextView price = getActivity().findViewById(R.id.customer_item_price);
                                price.setText(document.getString("price"));

                                TextView rating = getActivity().findViewById(R.id.customer_item_rating);
                                rating.setText(document.getLong("rating").toString());

                                TextView range = getActivity().findViewById(R.id.customer_item_range);
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