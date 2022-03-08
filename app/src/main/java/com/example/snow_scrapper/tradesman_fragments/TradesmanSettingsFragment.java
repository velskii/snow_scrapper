package com.example.snow_scrapper.tradesman_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.snow_scrapper.LoginActivity;
import com.example.snow_scrapper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TradesmanSettingsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "TradesmanSettingsFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        getUserInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tradesman_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button updLink = getActivity().findViewById(R.id.update_link_tradesman);
        updLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI("update");
            }
        });

        Button updBtn = getActivity().findViewById(R.id.setting_save_tradesman);
        updBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editUsername = getActivity().findViewById(R.id.setting_username_tradesman);
                String username = editUsername.getText().toString();

                EditText editPassword = getActivity().findViewById(R.id.setting_password_tradesman);
                String pwd = editPassword.getText().toString();

                EditText editCity = getActivity().findViewById(R.id.setting_city_tradesman);
                String city = editCity.getText().toString();

                EditText editAddress = getActivity().findViewById(R.id.setting_address_tradesman);
                String address = editAddress.getText().toString();

                EditText editPostalCode = getActivity().findViewById(R.id.setting_postal_code_tradesman);
                String postal_code = editPostalCode.getText().toString();

                EditText editTelephone = getActivity().findViewById(R.id.setting_telephone_tradesman);
                String telephone = editTelephone.getText().toString();

                EditText editEmail = getActivity().findViewById(R.id.setting_email_tradesman);
                String email = editEmail.getText().toString();

                if (username.isEmpty()) {
                    editUsername.setError("Please input username");
                    return;
                } else if (pwd.isEmpty()) {
                    editPassword.setError("Please input password");
                    return;
                } else if (city.isEmpty()) {
                    editCity.setError("Please input city name");
                    return;
                } else if (address.isEmpty()) {
                    editAddress.setError("Please input address");
                    return;
                } else if (postal_code.isEmpty()) {
                    editPostalCode.setError("Please input postal code");
                    return;
                } else if (telephone.isEmpty()) {
                    editTelephone.setError("Please input telephone");
                    return;
                } else if (email.isEmpty()) {
                    editEmail.setError("Please input email address");
                    return;
                }
                updateSettings(username, pwd, city, address, postal_code, telephone, email);

            }
        });
    }



    private void getUserInfo(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        String uid = currentUser.getUid();
        db.collection("users")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TextView username = getActivity().findViewById(R.id.username_show_tradesman);
                                username.setText("Username:    " + document.getData().get("username").toString());

                                EditText editUsername = getActivity().findViewById(R.id.setting_username_tradesman);
                                editUsername.setHint("Username: " + document.getData().get("username").toString());

                                TextView pwd = getActivity().findViewById(R.id.password_show_tradesman);
                                pwd.setText("Password:    " + document.getData().get("password").toString());


                                EditText editPwd = getActivity().findViewById(R.id.setting_password_tradesman);
                                editPwd.setHint("Password: " + document.getData().get("password").toString());


                                TextView city = getActivity().findViewById(R.id.city_show_tradesman);
                                city.setText("City:           " + document.getData().get("city").toString());
                                EditText editCity = getActivity().findViewById(R.id.setting_city_tradesman);
                                editCity.setHint("City: " + document.getData().get("city").toString());

                                TextView address = getActivity().findViewById(R.id.address_show_tradesman);
                                address.setText("Address:    " + document.getData().get("address").toString());
                                EditText editAddress = getActivity().findViewById(R.id.setting_address_tradesman);
                                editAddress.setHint("Address: " + document.getData().get("address").toString());



                                TextView postal_code = getActivity().findViewById(R.id.postal_code_show_tradesman);
                                postal_code.setText("Postal Code: " + document.getData().get("postal_code").toString());
                                EditText editPostalCode = getActivity().findViewById(R.id.setting_postal_code_tradesman);
                                editPostalCode.setHint("Postal Code: " + document.getData().get("postal_code").toString());

                                TextView telephone = getActivity().findViewById(R.id.telephone_show_tradesman);
                                telephone.setText("Telephone:  " + document.getData().get("telephone").toString());
                                EditText editTelephone = getActivity().findViewById(R.id.setting_telephone_tradesman);
                                editTelephone.setHint("Telephone: " + document.getData().get("telephone").toString());

                                TextView email = getActivity().findViewById(R.id.email_show_tradesman);
                                email.setText("Email:      " + document.getData().get("email").toString());
                                EditText editEmail = getActivity().findViewById(R.id.setting_email_tradesman);
                                editEmail.setHint("Email: " + document.getData().get("email").toString());



                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void updateUI(String type) {
        LinearLayout show = getActivity().findViewById(R.id.setting_show_tradesman);
        LinearLayout upd = getActivity().findViewById(R.id.setting_update_tradesman);
        if(type == "show") {
            getUserInfo();
            show.setVisibility(View.VISIBLE);
            upd.setVisibility(View.INVISIBLE);

        } else {
            show.setVisibility(View.INVISIBLE);
            upd.setVisibility(View.VISIBLE);
        }
    }

    public void updateSettings( String username, String password, String city, String address, String postal_code, String telephone, String email ) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        String uid = currentUser.getUid();

        db.collection("users").document(uid)
                .update(
                        "username", username,
                        "password", password,
                        "city", city,
                        "address", address,
                        "postal_code", postal_code,
                        "telephone", telephone,
                        "email", email
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateUI("show");
//                reload();
            }
        });


    }
}