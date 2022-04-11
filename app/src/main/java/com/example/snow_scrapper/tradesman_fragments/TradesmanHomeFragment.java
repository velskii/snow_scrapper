package com.example.snow_scrapper.tradesman_fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.snow_scrapper.LoginActivity;
import com.example.snow_scrapper.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradesmanHomeFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private static final String TAG = "TradesmanHomeFragment";
    FirebaseStorage storage;
    StorageReference storageReference;
    private Button btnChoose, btnUpload;
    private ImageView imageView;

    private Uri filePath;
    private Uri uploadedImgUri;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tradesman_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnChoose = (Button) getActivity().findViewById(R.id.btnChoose);
        btnUpload = (Button) getActivity().findViewById(R.id.btnUpload);
        imageView = (ImageView) getActivity().findViewById(R.id.imgView);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        Button btn = getActivity().findViewById(R.id.add_service);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit_service_name = getActivity().findViewById(R.id.service_name);
                String service_name = edit_service_name.getText().toString();

                EditText edit_service_type = getActivity().findViewById(R.id.service_type);
                String service_type = edit_service_type.getText().toString();

                EditText edit_service_price = getActivity().findViewById(R.id.service_price);
                String service_price = edit_service_price.getText().toString();

//                EditText edit_service_image = getActivity().findViewById(R.id.service_image);
//                String service_image = edit_service_image.getText().toString();

                EditText edit_service_location = getActivity().findViewById(R.id.service_location);
                String service_location = edit_service_location.getText().toString();

                EditText edit_service_range = getActivity().findViewById(R.id.service_range);
                String service_range = edit_service_range.getText().toString();

                if (service_name.isEmpty()) {
                        edit_service_name.setError("Please input service name");
                } else if (service_type.isEmpty()) {
                    edit_service_type.setError("Please input service price");
                } else if (service_price.isEmpty()) {
                    edit_service_price.setError("Please input service price");
//                } else if (service_image.isEmpty()) {
//                    edit_service_image.setError("Please upload service image");
                } else if (service_location.isEmpty()) {
                    edit_service_location.setError("Please input service location");
                } else if (service_range.isEmpty()) {
                    edit_service_range.setError("Please input service range");
                } else {
                    String service_image = "";
                    storeServiceData(service_name, service_type, service_price, service_image, service_location, service_range);
                }

            }
        });

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

    public void storeServiceData(String service_name,String service_type, String service_price, String service_image, String service_location, String service_range) {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = "";
        if(currentUser != null){
            uid = currentUser.getUid();
        }
        Map<String, Object> service = new HashMap<>();
        service.put("name", service_name);
        service.put("service_type", service_type);
        service.put("price", service_price);
//        service.put("image", service_image);
        service.put("image", uploadedImgUri.toString());
        service.put("location", service_location);
        service.put("range", service_range);

        service.put("publisher", uid);

        Double discount = 1.0;

        service.put("sale_volume", 0);
        service.put("discount", discount);
        service.put("rating", 5);
        service.put("current_price", Double.valueOf(service_price) * discount);

        service.put("created_time", Calendar.getInstance().getTime());
        service.put("updated_time", Calendar.getInstance().getTime());

        // Add a new document with a generated ID
        db.collection("service_list")
                .add(service)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        updateUI();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        // [END add_ada_lovelace]
    }

    private void updateUI() {

        BottomNavigationView bnv = getActivity().findViewById(R.id.tradesman_bottom_navigation);

        bnv.setSelectedItemId(R.id.tradesman_service_list);

//        Fragment fragment = new TradesmanServiceListFragment();
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName())
//                .commit();
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName());
//        transaction.commit();


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog( getContext() );
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUri = uri;
                                    Log.d("Zhou", "img: downloadUri: " + downloadUri);
                                    uploadedImgUri = downloadUri;
                                }
                            });

                            Toast.makeText( getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText( getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });


        }
    }
}