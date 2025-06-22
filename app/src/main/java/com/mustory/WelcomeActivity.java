package com.mustory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class WelcomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
   DrawerLayout drawerLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.welcome);
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        // Toggle initialization
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout,toolbar, R.string.open_navigation, R.string.close_navigation
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Fragment initialization
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, new HomeFragment())
//                    .commit();
//        } else if (id == R.id.nav_playlist) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container,new H())
//                    .commit();
//        }  else {
//            return false;
//
//        }
//        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}