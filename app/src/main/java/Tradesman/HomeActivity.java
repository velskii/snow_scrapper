package Tradesman;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.example.snow_scrapper.LoginActivity;
import com.example.snow_scrapper.R;
import com.example.snow_scrapper.tradesman_fragments.TradesmanHomeFragment;
import com.example.snow_scrapper.tradesman_fragments.TradesmanMessagesFragment;
import com.example.snow_scrapper.tradesman_fragments.TradesmanOrdersFragment;
import com.example.snow_scrapper.tradesman_fragments.TradesmanSettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradesmen_home);
        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView bnv = findViewById(R.id.tradesman_bottom_navigation);


        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.tradesman_messages:
                        fragment = new TradesmanMessagesFragment();
                        break;
                    case R.id.tradesman_orders:
                        fragment = new TradesmanOrdersFragment();
                        break;
                    case R.id.tradesman_settings:
                        fragment = new TradesmanSettingsFragment();
                        break;
                    default:
                        fragment = new TradesmanHomeFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName())
                        .commit();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName());
                transaction.commit();
                return true;
            };
        });
        bnv.setSelectedItemId(R.id.tradesman_home);

    }



}