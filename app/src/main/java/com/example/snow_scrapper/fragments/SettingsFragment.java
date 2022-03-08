package com.example.snow_scrapper.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public class SettingsFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private static final String TAG = "SettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        getUserInfo();
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
                                TextView username = getActivity().findViewById(R.id.username_show);
                                username.setText("Username:    " + document.getData().get("username").toString());

                                EditText editUsername = getActivity().findViewById(R.id.setting_username);
                                editUsername.setHint("Username: " + document.getData().get("username").toString());

                                TextView pwd = getActivity().findViewById(R.id.password_show);
                                pwd.setText("Password:    " + document.getData().get("password").toString());


                                EditText editPwd = getActivity().findViewById(R.id.setting_password);
                                editPwd.setHint("Password: " + document.getData().get("password").toString());


                                TextView city = getActivity().findViewById(R.id.city_show);
                                city.setText("City:           " + document.getData().get("city").toString());


                                EditText editCity = getActivity().findViewById(R.id.setting_city);
                                editCity.setHint("City: " + document.getData().get("city").toString());


                                TextView address = getActivity().findViewById(R.id.address_show);
                                address.setText("Address:    " + document.getData().get("address").toString());


                                EditText editAddress = getActivity().findViewById(R.id.setting_address);
                                editAddress.setHint("Address: " + document.getData().get("address").toString());


                                TextView postal_code = getActivity().findViewById(R.id.postal_code_show);
                                postal_code.setText("Postal Code: " + document.getData().get("postal_code").toString());

                                EditText editPostalCode = getActivity().findViewById(R.id.setting_postal_code);
                                editPostalCode.setHint("Postal Code: " + document.getData().get("postal_code").toString());


                                TextView telephone = getActivity().findViewById(R.id.telephone_show);
                                telephone.setText("Telephone:  " + document.getData().get("telephone").toString());

                                EditText editTelephone = getActivity().findViewById(R.id.setting_telephone);
                                editTelephone.setHint("Telephone: " + document.getData().get("telephone").toString());


                                TextView email = getActivity().findViewById(R.id.email_show);
                                email.setText("Email:      " + document.getData().get("email").toString());

                                EditText editEmail = getActivity().findViewById(R.id.setting_email);
                                editEmail.setHint("Email: " + document.getData().get("email").toString());

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button updLink = getActivity().findViewById(R.id.update_link);
        updLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI("update");
            }
        });

        Button updBtn = getActivity().findViewById(R.id.setting_save);
        updBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editUsername = getActivity().findViewById(R.id.setting_username);
                String username = editUsername.getText().toString();

                EditText editPassword = getActivity().findViewById(R.id.setting_password);
                String pwd = editPassword.getText().toString();

                EditText editCity = getActivity().findViewById(R.id.setting_city);
                String city = editCity.getText().toString();

                EditText editAddress = getActivity().findViewById(R.id.setting_address);
                String address = editAddress.getText().toString();

                EditText editPostalCode = getActivity().findViewById(R.id.setting_postal_code);
                String postal_code = editPostalCode.getText().toString();

                EditText editTelephone = getActivity().findViewById(R.id.setting_telephone);
                String telephone = editTelephone.getText().toString();

                EditText editEmail = getActivity().findViewById(R.id.setting_email);
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

    private void updateUI(String type) {
        LinearLayout show = getActivity().findViewById(R.id.setting_show);
        LinearLayout upd = getActivity().findViewById(R.id.setting_update);
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

    private void reload() {
//        Fragment fragment = null;
//        Class fragmentClass;
//        fragmentClass = SettingsFragment.class;
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        Fragment frg = null;
        frg = getActivity().getSupportFragmentManager().findFragmentByTag("SettingsFragment");
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

}