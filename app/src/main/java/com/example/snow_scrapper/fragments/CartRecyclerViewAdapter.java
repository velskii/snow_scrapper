package com.example.snow_scrapper.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snow_scrapper.Db;
import com.example.snow_scrapper.DbListenerInterface;
import modals.CartDeleteDialogFragment;

import com.example.snow_scrapper.HomeActivity;
import com.example.snow_scrapper.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartRecyclerViewAdapter  extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {
    private List<Map<String, String>> mDataSet;
    private static final String TAG = "CartRecyclerViewAdapter";

    public CartRecyclerViewAdapter(List<Map<String, String>> dataSet) {
        mDataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView displayId;
        private final ImageView btnDelete;
        private final ImageView btnPurchase;
        private final TextView displayName;
        private final TextView displayPrice;
        private final ImageView displayImage;

        private static final String TAG = "CartRecyclerViewHolder";

        public ViewHolder(View v) {
            super(v);

            displayId = (TextView) v.findViewById(R.id.cart_item_id);
            btnPurchase = (ImageView) v.findViewById(R.id.cart_item_purchase);
            btnDelete = (ImageView) v.findViewById(R.id.cart_item_delete);

            displayName = (TextView) v.findViewById(R.id.cart_item_name);
            displayPrice = (TextView) v.findViewById(R.id.cart_item_price);
            displayImage = (ImageView) v.findViewById(R.id.cart_item_image);

        }

        public TextView getDisplayId() {
            return displayId;
        }
        public ImageView getBtnDelete() {
            return btnDelete;
        }
        public ImageView getBtnPurchase() {
            return btnPurchase;
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
        viewHolder.getDisplayId().setText(mDataSet.get(position).get("id"));
        viewHolder.getDisplayName().setText("service name: " + mDataSet.get(position).get("name"));
        viewHolder.getDisplayprice().setText("price: " + mDataSet.get(position).get("price") + " $");
        viewHolder.getDisplayImage().setImageResource(R.drawable.snow1);

        Map<String, Object> orderDetail = new HashMap<>();
        orderDetail.put("name", mDataSet.get(position).get("name"));
        orderDetail.put("price", mDataSet.get(position).get("price") + " $");
        orderDetail.put("location", mDataSet.get(position).get("location"));
        orderDetail.put("seller", mDataSet.get(position).get("uid"));
        orderDetail.put("image", mDataSet.get(position).get("image"));
        orderDetail.put("range", mDataSet.get(position).get("range"));
        orderDetail.put("rating", mDataSet.get(position).get("rating"));
        orderDetail.put("service_id", mDataSet.get(position).get("service_id"));


        orderDetail.put("created_at", Calendar.getInstance().getTime());
        orderDetail.put("updated_at", Calendar.getInstance().getTime());

        viewHolder.getBtnDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert("Delete information!", "Are you sure to delete this item?", v, position, "delete", orderDetail);
            }
        });

        viewHolder.getBtnPurchase().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert("Purchase item", "are you sure to purchase this item?", v, position, "purchase", orderDetail);

            }
        });
    }

    public void alert(String title, String msg, View v, int position, String flag, Map<String, Object> orderDetail){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        new Db().deleteCartDataByUid(mDataSet.get(position).get("id"), new DbListenerInterface(){
                            @Override
                            public void onStart() {

                            }
                            @Override
                            public void onSuccess(List data) {
                                removeAt( position );
                                if (flag == "purchase"){
                                    addOrder( orderDetail, v );
                                }
                            }

                            @Override
                            public void onFailed(String databaseError) {

                            }
                        });
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void addOrder( Map<String, Object> orderDetail, View v ){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders")
                .add(orderDetail)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        fragmentJump(v, documentReference.getId());
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
    public int getItemCount() {
        return mDataSet.size();
    }


    public void removeAt(int position) {
        mDataSet.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataSet.size());
    }

    private void fragmentJump(View v, String id) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();

        BottomNavigationView bnv = activity.findViewById(R.id.bottom_navigation);
        bnv.setSelectedItemId(R.id.orders);

        Fragment fragment = new OrderDetailsFragment( id );
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName())
                .commit();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

}
