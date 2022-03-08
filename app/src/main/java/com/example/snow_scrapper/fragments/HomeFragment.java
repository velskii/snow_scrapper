package com.example.snow_scrapper.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.snow_scrapper.R;
import com.example.snow_scrapper.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends androidx.fragment.app.Fragment {

    private SliderLayout sliderShow;
    private FirebaseFirestore db;
    private static final String TAG = "HomeFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }
    protected HomeFragment.LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected RecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<Map<String, String>> listOfMaps = new ArrayList<Map<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        getServiceList(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_home, container, false);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.service_list);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sliderShow = (SliderLayout) view.findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.snow1);
        file_maps.put("Big Bang Theory",R.drawable.s1);
        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this.sliderListener());

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            sliderShow.addSlider(textSliderView);
        }

//        ImageView item = view.findViewById(R.id.item_image);
//        item.setImageResource(R.drawable.bigbang);

//        TextView discountIndicator = view.findViewById(R.id.discount_indicator);
//        discountIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }

    private BaseSliderView.OnSliderClickListener sliderListener(){
        Toast.makeText(getContext(), sliderShow.getId()+" Clicked", Toast.LENGTH_LONG);
        return null;
    }

    public void getServiceList(Bundle savedInstanceState) {

        this.db.collection("service_list")
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
                            mCurrentLayoutManagerType = HomeFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                            if (savedInstanceState != null) {
                                mCurrentLayoutManagerType = (HomeFragment.LayoutManagerType) savedInstanceState
                                        .getSerializable(KEY_LAYOUT_MANAGER);
                            }
                            setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                            mAdapter = new RecyclerViewAdapter(listOfMaps);
                            mRecyclerView.setAdapter(mAdapter);

                            Log.d("Zhou22", listOfMaps.toString());
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void setRecyclerViewLayoutManager(HomeFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = HomeFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

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