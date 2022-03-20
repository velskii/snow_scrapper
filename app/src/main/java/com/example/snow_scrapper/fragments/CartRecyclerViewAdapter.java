package com.example.snow_scrapper.fragments;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.snow_scrapper.R;
import com.example.snow_scrapper.RecyclerViewAdapter;
import java.util.List;
import java.util.Map;

public class CartRecyclerViewAdapter  extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {
    private List<Map<String, String>> mDataSet;
    private static final String TAG = "CartRecyclerViewAdapter";

    public CartRecyclerViewAdapter(List<Map<String, String>> dataSet) {
        mDataSet = dataSet;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView displayName;
        private final TextView displayPrice;
        private final ImageView displayImage;

        private static final String TAG = "RecyclerViewHolder";

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                private final TextView textView = null;
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            displayName = (TextView) v.findViewById(R.id.cart_item_name);
            displayPrice = (TextView) v.findViewById(R.id.cart_item_price);
            displayImage = (ImageView) v.findViewById(R.id.cart_item_image);
            ImageView btnPurchase = (ImageView) v.findViewById(R.id.cart_item_purchase);

            btnPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Zhou777", String.valueOf(getAdapterPosition()));
                }
            });
        }

        public TextView getDisplayName() {
            return displayName;
        }
        public TextView getDisplayprice() {
            return displayPrice;
        }
        public ImageView getDisplayImage() {
            return displayImage;
        }
    }

    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cart_list_item, viewGroup, false);

        return new CartRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CartRecyclerViewAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        Log.d(TAG, "Element " + position + " set.");
        viewHolder.getDisplayName().setText(mDataSet.get(position).get("name"));
        viewHolder.getDisplayprice().setText(mDataSet.get(position).get("price"));
        viewHolder.getDisplayImage().setImageResource(R.drawable.snow1);

    }
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
