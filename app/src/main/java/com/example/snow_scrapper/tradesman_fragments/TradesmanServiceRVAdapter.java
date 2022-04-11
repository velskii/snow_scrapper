package com.example.snow_scrapper.tradesman_fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snow_scrapper.ImageLoadTask;
import com.example.snow_scrapper.R;

import java.util.List;
import java.util.Map;

public class TradesmanServiceRVAdapter extends RecyclerView.Adapter<TradesmanServiceRVAdapter.ViewHolder>{


    private List<Map<String, String>> mDataSet;
    private static final String TAG = "TradesmanServiceRVAdapter";

    public TradesmanServiceRVAdapter(List<Map<String, String>> dataSet) {
        mDataSet = dataSet;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView id;
        private final TextView displayName;
        private final TextView displayPrice;
        private final TextView displayLocation;
        private final TextView displayRange;
        private final ImageView displayImage;

        private static final String TAG = "TradesmanServiceRVAdapterHolder";

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                private final TextView textView = null;
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");


//                    Intent myIntent = new Intent(v.getContext(), ServiceDetailsActivity.class);
//                    myIntent.putExtra("item_id", getId().toString());
//
//
//                    v.getContext().startActivity(myIntent);
                }
            });
            id = (TextView) v.findViewById(R.id.item_id);
            displayName = (TextView) v.findViewById(R.id.item_name);
            displayPrice = (TextView) v.findViewById(R.id.item_price);
            displayLocation = (TextView) v.findViewById(R.id.item_location);
            displayRange = (TextView) v.findViewById(R.id.item_range);
            displayImage = (ImageView) v.findViewById(R.id.item_image);
//            v.findViewById(R.id.discount_indicator).setVisibility(View.INVISIBLE);
        }

        public TextView getId() {
            return id;
        }
        public TextView getDisplayName() {
            return displayName;
        }
        public TextView getDisplayprice() {
            return displayPrice;
        }
        public TextView getDisplayLocation() {
            return displayLocation;
        }
        public TextView getDisplayRange() {
            return displayRange;
        }
        public ImageView getDisplayImage() {
            return displayImage;
        }
    }

    @Override
    public TradesmanServiceRVAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);

        return new TradesmanServiceRVAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TradesmanServiceRVAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        Log.d(TAG, "Element " + position + " set.");
        viewHolder.getDisplayName().setText(mDataSet.get(position).get("name"));
        viewHolder.getDisplayprice().setText(mDataSet.get(position).get("price"));
        viewHolder.getDisplayLocation().setText(mDataSet.get(position).get("location"));
        viewHolder.getDisplayRange().setText(mDataSet.get(position).get("range"));
        //https://firebasestorage.googleapis.com/v0/b
        if (mDataSet.get(position).get("image").contains("https://firebasestorage.googleapis.com/")) {

            new ImageLoadTask( mDataSet.get(position).get("image"), viewHolder.getDisplayImage() ).execute();

//            viewHolder.getDisplayImage().setImageURI( Uri.parse(mDataSet.get(position).get("image"))  );
        } else {
            viewHolder.getDisplayImage().setImageResource(R.drawable.snow1);
        }


        viewHolder.getId().setText(mDataSet.get(position).get("id"));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new TradesmanServiceDetailsFragment( mDataSet.get(position).get("id") );
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName())
                        .commit();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName());
                transaction.commit();


//                Intent myIntent = new Intent(v.getContext(), ServiceDetailsActivity.class);
//                myIntent.putExtra("item_id", mDataSet.get(position).get("id"));
//
//                v.getContext().startActivity(myIntent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
