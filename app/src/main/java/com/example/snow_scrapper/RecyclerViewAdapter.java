package com.example.snow_scrapper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Map<String, String>> mDataSet;
    private static final String TAG = "RecyclerViewAdapter";

    public RecyclerViewAdapter(List<Map<String, String>> dataSet) {
        mDataSet = dataSet;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView id;
        private final TextView displayName;
        private final TextView displayPrice;
        private final TextView displayLocation;
        private final TextView displayRange;
        private final ImageView displayImage;

        private static final String TAG = "RecyclerViewHolder";

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
            v.findViewById(R.id.discount_indicator).setVisibility(View.INVISIBLE);
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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        Log.d(TAG, "Element " + position + " set.");
        viewHolder.getDisplayName().setText(mDataSet.get(position).get("name"));
        viewHolder.getDisplayprice().setText(mDataSet.get(position).get("price"));
        viewHolder.getDisplayLocation().setText(mDataSet.get(position).get("location"));
        viewHolder.getDisplayRange().setText(mDataSet.get(position).get("range"));
        viewHolder.getDisplayImage().setImageResource(R.drawable.snow1);

        viewHolder.getId().setText(mDataSet.get(position).get("id"));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), ServiceDetailsActivity.class);
                myIntent.putExtra("item_id", mDataSet.get(position).get("id"));

                v.getContext().startActivity(myIntent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

