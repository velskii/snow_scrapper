package com.example.snow_scrapper;
import com.google.firebase.firestore.SnapshotMetadata;

import java.util.List;

public interface DbListenerInterface {

    public void onStart();
    public void onSuccess(List data);
    public void onFailed(String databaseError);

}
