package com.example.snow_scrapper.tradesman_fragments;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snow_scrapper.R;

import java.util.List;
import java.util.Map;

public class TradesmanOrderRecyclerViewAdapter extends RecyclerView.Adapter<TradesmanOrderRecyclerViewAdapter.ViewHolder> {


    private List<Map<String, String>> mDataSet;
    private static final String TAG = "TradesmanOrderRecyclerViewAdapter";

    public TradesmanOrderRecyclerViewAdapter(List<Map<String, String>> dataSet) {
        mDataSet = dataSet;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView displayId;
        private final TextView displayName;
        private final TextView displayTime;
        private final TextView displayLocation;

        private static final String TAG = "TradesmanOrderRecyclerViewHolder";

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                private final TextView textView = null;
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            displayName = (TextView) v.findViewById(R.id.order_item_name);
            displayId = (TextView) v.findViewById(R.id.order_item_id);
            displayTime = (TextView) v.findViewById(R.id.order_item_created_time);
            displayLocation = (TextView) v.findViewById(R.id.order_item_location);

        }

        public TextView getDisplayId() {
            return displayId;
        }
        public TextView getDisplayName() {
            return displayName;
        }
        public TextView getDisplayTime() {
            return displayTime;
        }
        public TextView getDisplayLocation() {
            return displayLocation;
        }
    }

    @Override
    public TradesmanOrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_list_item, viewGroup, false);

        return new TradesmanOrderRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TradesmanOrderRecyclerViewAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        Log.d(TAG, "Element " + position + " set.");
        viewHolder.getDisplayId().setText("Order ID: " + mDataSet.get(position).get("id"));
        viewHolder.getDisplayName().setText("Service name: " + mDataSet.get(position).get("name"));
        viewHolder.getDisplayTime().setText("Order time: " + mDataSet.get(position).get("created_time"));
        viewHolder.getDisplayLocation().setText("Location: " + mDataSet.get(position).get("location"));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new TradesmanOrderDetailsFragment( mDataSet.get(position).get("id") );
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName())
                        .commit();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName());
                transaction.commit();

            }
        });

    }
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
