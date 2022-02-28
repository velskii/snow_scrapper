package com.example.snow_scrapper.tradesman_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.snow_scrapper.LoginActivity;
import com.example.snow_scrapper.R;
import com.google.firebase.auth.FirebaseAuth;

public class TradesmanSettingsFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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
        Button signOutBtn = getActivity().findViewById(R.id.tradesman_sign_out);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
        startActivity(new Intent(getContext(), LoginActivity.class));
    }
}