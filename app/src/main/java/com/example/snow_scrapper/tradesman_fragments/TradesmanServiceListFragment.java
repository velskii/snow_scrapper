package com.example.snow_scrapper.tradesman_fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.snow_scrapper.Db;
import com.example.snow_scrapper.DbListenerInterface;
import com.example.snow_scrapper.R;
import com.example.snow_scrapper.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getData(savedInstanceState, "new");

        TextView btn_reset = getActivity().findViewById(R.id.clear_sort);
        TextView btn_sort_by = getActivity().findViewById(R.id.sort_by);
        TextView btn_new = getActivity().findViewById(R.id.sort_by_new);


        TextView btn_bestseller = (TextView) getActivity().findViewById(R.id.sort_by_bestseller);
        TextView btn_price_lowest = getActivity().findViewById(R.id.sort_by_price_lowest);
        TextView btn_price_highest = getActivity().findViewById(R.id.sort_by_price_highest);
        TextView btn_discount = getActivity().findViewById(R.id.sort_by_discount);
        TextView btn_rating_highest = getActivity().findViewById(R.id.sort_by_rating_highest);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_sort_by.setText("Sort By New");
                btn_new.setBackgroundResource(R.color.logo_green_color);

                btn_bestseller.setBackgroundResource(R.color.light_grey);
                btn_price_lowest.setBackgroundResource(R.color.light_grey);
                btn_price_highest.setBackgroundResource(R.color.light_grey);
                btn_discount.setBackgroundResource(R.color.light_grey);
                btn_rating_highest.setBackgroundResource(R.color.light_grey);
            }
        });

        btn_bestseller.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                btn_sort_by.setText("Sort By Bestseller");
                btn_bestseller.setBackgroundResource(R.color.logo_green_color);

                btn_new.setBackgroundResource(R.color.light_grey);
                btn_price_lowest.setBackgroundResource(R.color.light_grey);
                btn_price_highest.setBackgroundResource(R.color.light_grey);
                btn_discount.setBackgroundResource(R.color.light_grey);
                btn_rating_highest.setBackgroundResource(R.color.light_grey);

//                getServiceList(savedInstanceState, "bestseller");
                showDataInRecycler(savedInstanceState, "bestseller");
            }
        });

        btn_discount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                btn_sort_by.setText("Sort By Discount");
                btn_discount.setBackgroundResource(R.color.logo_green_color);

                btn_new.setBackgroundResource(R.color.light_grey);
                btn_price_lowest.setBackgroundResource(R.color.light_grey);
                btn_price_highest.setBackgroundResource(R.color.light_grey);
                btn_bestseller.setBackgroundResource(R.color.light_grey);
                btn_rating_highest.setBackgroundResource(R.color.light_grey);
                showDataInRecycler(savedInstanceState, "discount");
            }
        });


        btn_new.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                btn_sort_by.setText("Sort By New");
                btn_new.setBackgroundResource(R.color.logo_green_color);

                btn_discount.setBackgroundResource(R.color.light_grey);
                btn_price_lowest.setBackgroundResource(R.color.light_grey);
                btn_price_highest.setBackgroundResource(R.color.light_grey);
                btn_bestseller.setBackgroundResource(R.color.light_grey);
                btn_rating_highest.setBackgroundResource(R.color.light_grey);
                showDataInRecycler(savedInstanceState, "new");

            }
        });

        btn_price_lowest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                btn_sort_by.setText("Sort By Price Lowest");
                btn_price_lowest.setBackgroundResource(R.color.logo_green_color);

                btn_discount.setBackgroundResource(R.color.light_grey);
                btn_new.setBackgroundResource(R.color.light_grey);
                btn_price_highest.setBackgroundResource(R.color.light_grey);
                btn_bestseller.setBackgroundResource(R.color.light_grey);
                btn_rating_highest.setBackgroundResource(R.color.light_grey);
                showDataInRecycler(savedInstanceState, "price_lowest");

            }
        });

        btn_price_highest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                btn_sort_by.setText("Sort By Price Highest");
                btn_price_highest.setBackgroundResource(R.color.logo_green_color);

                btn_discount.setBackgroundResource(R.color.light_grey);
                btn_new.setBackgroundResource(R.color.light_grey);
                btn_price_lowest.setBackgroundResource(R.color.light_grey);
                btn_bestseller.setBackgroundResource(R.color.light_grey);
                btn_rating_highest.setBackgroundResource(R.color.light_grey);
                showDataInRecycler(savedInstanceState, "price_highest");

            }
        });

        btn_rating_highest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                btn_sort_by.setText("Sort By Rating Highest");
                btn_rating_highest.setBackgroundResource(R.color.logo_green_color);

                btn_discount.setBackgroundResource(R.color.light_grey);
                btn_new.setBackgroundResource(R.color.light_grey);
                btn_price_lowest.setBackgroundResource(R.color.light_grey);
                btn_bestseller.setBackgroundResource(R.color.light_grey);
                btn_bestseller.setBackgroundResource(R.color.light_grey);
                showDataInRecycler(savedInstanceState, "rating");
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


    }

    public void getData(Bundle savedInstanceState, String sort_by){
        new Db().getServiceListBySort(mAuth.getUid(), new DbListenerInterface() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(List data) {
                listOfMaps = data;
                TextView filter = getActivity().findViewById(R.id.filter_title);
                filter.setText("Service Items: "+ listOfMaps.size());
                showDataInRecycler(savedInstanceState, "new");
            }

            @Override
            public void onFailed(String databaseError) {

            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tradesman_service_list, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.service_list);
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDataInRecycler( Bundle savedInstanceState, String sort_by){

        switch (sort_by){
            case "new":
                listOfMaps.sort(new Comparator<Map<String, String>>() {
                    @Override
                    public int compare(Map<String, String> o1, Map<String, String> o2) {
                        return o1.get("created_time").compareTo(o2.get("created_time"));
//                                    return 0;
                    }
                });
                break;
            case "best_seller":
                listOfMaps.sort(new Comparator<Map<String, String>>() {
                    @Override
                    public int compare(Map<String, String> o1, Map<String, String> o2) {
                        return o1.get("sale_volume").compareTo(o2.get("sale_volume"));
//                                    return 0;
                    }
                });
                break;
            case "discount":
                listOfMaps.sort(new Comparator<Map<String, String>>() {
                    @Override
                    public int compare(Map<String, String> o1, Map<String, String> o2) {
                        return o1.get("discount").compareTo(o2.get("discount"));
                    }
                });
                break;
            case "price_lowest":
                listOfMaps.sort(new Comparator<Map<String, String>>() {
                    @Override
                    public int compare(Map<String, String> o1, Map<String, String> o2) {
                        return o1.get("price").compareTo(o2.get("price"));
                    }
                });
                break;
            case "price_highest":
                listOfMaps.sort(new Comparator<Map<String, String>>() {
                    @Override
                    public int compare(Map<String, String> o1, Map<String, String> o2) {
                        return o2.get("price").compareTo(o1.get("price"));
                    }
                });
                break;
            case "rating_highest":
                listOfMaps.sort(new Comparator<Map<String, String>>() {
                    @Override
                    public int compare(Map<String, String> o1, Map<String, String> o2) {
                        return o1.get("rating").compareTo(o2.get("rating"));
                    }
                });
                break;

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