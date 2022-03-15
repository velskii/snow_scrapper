package com.example.snow_scrapper;

import android.os.Build;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Db {

    private static final String TAG = "Db object";
    private FirebaseFirestore db;
    protected List<Map<String, String>> listOfMaps = new ArrayList<Map<String, String>>();



//    public void Db(){
//        db = FirebaseFirestore.getInstance();
//    }

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
