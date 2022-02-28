package com.example.snow_scrapper.tradesman_fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.snow_scrapper.R;
import com.example.snow_scrapper.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradesmanServiceListFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private static final String TAG = "TradesmanServiceListFragment";

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected RecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<Map<String, String>> listOfMaps = new ArrayList<Map<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        getServiceList(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tradesman_service_list, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.service_list);
        return rootView;
    }


    public void getServiceList(Bundle savedInstanceState) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = "";
        if(currentUser != null){
            uid = currentUser.getUid();
        }

        this.db.collection("service_list")
                .whereEqualTo("publisher", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, String> service = new HashMap<>();
                                service.put( "name", document.getData().get("name").toString() );
                                service.put( "price", document.getData().get("price").toString() );
                                service.put( "image", document.getData().get("image").toString() );
                                service.put( "location", document.getData().get("location").toString() );
                                service.put( "range", document.getData().get("range").toString() );
                                service.put( "publisher", document.getData().get("publisher").toString() );
                                Log.d("Zhou11", service.toString());
                                listOfMaps.add(service);
                            }
                            mLayoutManager = new LinearLayoutManager(getActivity());

                            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

                            if (savedInstanceState != null) {
                                // Restore saved layout manager type.
                                mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                                        .getSerializable(KEY_LAYOUT_MANAGER);
                            }
                            setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

                            mAdapter = new RecyclerViewAdapter(listOfMaps);
                            // Set CustomAdapter as the adapter for RecyclerView.
                            mRecyclerView.setAdapter(mAdapter);
                            // END_INCLUDE(initializeRecyclerView)
                            Log.d("Zhou22", listOfMaps.toString());
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }
}