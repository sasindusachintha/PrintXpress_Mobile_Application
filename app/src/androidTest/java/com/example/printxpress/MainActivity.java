package com.example.print;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.print.activities.DesignGuidelinesActivity;
import com.example.print.firebase.RealtimeDatabaseHelper;
import com.example.print.fragments.HomeFragment;
import com.example.print.fragments.OrderHistoryFragment;
import com.example.print.fragments.ProfileFragment;
import com.example.print.models.PrintOrder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MainActivity extends AppCompatActivity {

    private final Fragment homeFragment = new HomeFragment();
    private final Fragment orderHistoryFragment = new OrderHistoryFragment();
    private final Fragment profileFragment = new ProfileFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);

        fm.beginTransaction().add(R.id.nav_host_fragment, profileFragment, "3").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, orderHistoryFragment, "2").hide(orderHistoryFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, homeFragment, "1").commit();

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                fm.beginTransaction().hide(active).show(homeFragment).commit();
                active = homeFragment;
                return true;
            } else if (itemId == R.id.nav_orders) {
                fm.beginTransaction().hide(active).show(orderHistoryFragment).commit();
                active = orderHistoryFragment;
                return true;
            } else if (itemId == R.id.nav_profile) {
                fm.beginTransaction().hide(active).show(profileFragment).commit();
                active = profileFragment;
                return true;
            }
            return false;
        });
    }

    public void navigateToDetail(Fragment fragment) {
        fm.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_guidelines) {
            startActivity(new Intent(this, DesignGuidelinesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
