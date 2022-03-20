package com.example.snow_scrapper;

import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.android.gms.tasks.OnCompleteListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Db {

    private static final String TAG = "Db object";
    private FirebaseFirestore db;
    protected List<Map<String, String>> listOfMaps = new ArrayList<Map<String, String>>();
    protected List<Map<String, String>> cartData = new ArrayList<Map<String, String>>();
    protected List<Map<String, String>> orderData = new ArrayList<Map<String, String>>();
    protected List<Map<String, String>> tradesmanOrderData = new ArrayList<Map<String, String>>();



//    public void Db(){
//        db = FirebaseFirestore.getInstance();
//    }

    public void getServiceList( final DbListenerInterface listener) {
        listener.onStart();

        db = FirebaseFirestore.getInstance();
        db.collection("service_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, String> service = new HashMap<>();
                                if (
                                        document.getData().get("created_time").toString() == ""
                                                || document.getData().get("updated_time").toString() == ""
                                                || document.getData().get("sale_volume").toString() == ""
                                                || document.getData().get("discount").toString() == ""
                                                || document.getData().get("rating").toString() == ""
                                                || document.getData().get("current_price").toString() == ""
                                ) {
                                    continue;
                                }

                                service.put( "id", document.getId() );
                                service.put( "name", document.getData().get("name").toString() );
                                service.put( "price", document.getData().get("price").toString() );
                                service.put( "image", document.getData().get("image").toString() );
                                service.put( "location", document.getData().get("location").toString() );
                                service.put( "range", document.getData().get("range").toString() );
                                service.put( "publisher", document.getData().get("publisher").toString() );


                                service.put( "created_time", document.getData().get("created_time").toString() );
                                service.put( "updated_time", document.getData().get("updated_time").toString() );
                                service.put( "sale_volume", document.getData().get("sale_volume").toString() );
                                service.put( "discount", document.getData().get("discount").toString() );
                                service.put( "rating", document.getData().get("rating").toString() );
                                service.put( "current_price", document.getData().get("current_price").toString() );

                                Log.d("Zhou11", service.toString());
                                listOfMaps.add(service);

                            }
                            listener.onSuccess(listOfMaps);

                        } else {
                            listener.onFailed("Error getting documents: ");
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getCartDataByUid( String uid, final DbListenerInterface listener) {
        listener.onStart();

        db = FirebaseFirestore.getInstance();
        db.collection("cart")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, String> cart = new HashMap<>();

                                cart.put( "id", document.getId() );
                                cart.put( "name", document.getData().get("name").toString() );
                                cart.put( "price", document.getData().get("price").toString() );
                                cart.put( "image", document.getData().get("image").toString() );

                                Log.d("Zhou11", cart.toString());
                                cartData.add(cart);

                            }
                            listener.onSuccess(cartData);

                        } else {
                            listener.onFailed("Error getting documents: ");
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getOrderDataByUid( String uid, final DbListenerInterface listener) {
        listener.onStart();

        db = FirebaseFirestore.getInstance();
        db.collection("orders")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, String> orders = new HashMap<>();

                                orders.put( "id", document.getId() );
                                orders.put( "name", document.getData().get("name").toString() );
                                orders.put( "price", document.getData().get("price").toString() );
                                orders.put( "location", document.getData().get("location").toString() );
                                orders.put( "range", document.getData().get("range").toString() );
                                orders.put( "rating", document.getData().get("rating").toString() );
                                orders.put( "image", document.getData().get("image").toString() );

                                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                                cal.setTimeInMillis( Long.parseLong( document.getData().get("created_time").toString().substring(18, 28)) * 1000);
                                String date = DateFormat.format("dd-MM-yyyy H:m:s", cal).toString();

                                orders.put( "created_time", date );
//                                Log.d("Zhou11", orders.toString());


                                orderData.add(orders);
                            }
                            listener.onSuccess(orderData);

                        } else {
                            listener.onFailed("Error getting documents: ");
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getOrderDataBySellerId( String seller_id, final DbListenerInterface listener) {
        listener.onStart();

        db = FirebaseFirestore.getInstance();
        db.collection("orders")
                .whereEqualTo("seller", seller_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, String> tradesmanOrders = new HashMap<>();

                                tradesmanOrders.put( "id", document.getId() );
                                tradesmanOrders.put( "name", document.getData().get("name").toString() );
                                tradesmanOrders.put( "price", document.getData().get("price").toString() );
                                tradesmanOrders.put( "location", document.getData().get("location").toString() );
                                tradesmanOrders.put( "range", document.getData().get("range").toString() );
                                tradesmanOrders.put( "rating", document.getData().get("rating").toString() );
                                tradesmanOrders.put( "image", document.getData().get("image").toString() );

                                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                                cal.setTimeInMillis( Long.parseLong( document.getData().get("created_time").toString().substring(18, 28)) * 1000);
                                String date = DateFormat.format("dd-MM-yyyy H:m:s", cal).toString();

                                tradesmanOrders.put( "created_time", date );
                                Log.d("Zhou5777", tradesmanOrders.toString());


                                tradesmanOrderData.add(tradesmanOrders);
                            }
                            listener.onSuccess(tradesmanOrderData);

                        } else {
                            listener.onFailed("Error getting documents: ");
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getServiceListBySort(String uid, final DbListenerInterface listener) {
        listener.onStart();

        db = FirebaseFirestore.getInstance();
        db.collection("service_list")
                .whereEqualTo("publisher", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, String> service = new HashMap<>();
                                if (
                                        document.getData().get("created_time").toString() == ""
                                                || document.getData().get("updated_time").toString() == ""
                                                || document.getData().get("sale_volume").toString() == ""
                                                || document.getData().get("discount").toString() == ""
                                                || document.getData().get("rating").toString() == ""
                                                || document.getData().get("current_price").toString() == ""
                                ) {
                                    continue;
                                }
                                service.put( "id", document.getId() );
                                service.put( "name", document.getData().get("name").toString() );
                                service.put( "price", document.getData().get("price").toString() );
                                service.put( "image", document.getData().get("image").toString() );
                                service.put( "location", document.getData().get("location").toString() );
                                service.put( "range", document.getData().get("range").toString() );
                                service.put( "publisher", document.getData().get("publisher").toString() );


                                service.put( "created_time", document.getData().get("created_time").toString() );
                                service.put( "updated_time", document.getData().get("updated_time").toString() );
                                service.put( "sale_volume", document.getData().get("sale_volume").toString() );
                                service.put( "discount", document.getData().get("discount").toString() );
                                service.put( "rating", document.getData().get("rating").toString() );
                                service.put( "current_price", document.getData().get("current_price").toString() );

                                Log.d("Zhou11", service.toString());
                                listOfMaps.add(service);

                            }
                            listener.onSuccess(listOfMaps);

                        } else {
                            listener.onFailed("Error getting documents: ");
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }






}
