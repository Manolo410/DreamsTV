package com.dreamsworld.dreamstv;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
        // Comment these lines out until you create the navigation resources
        // BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        // NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
        //         .findFragmentById(R.id.nav_host_fragment);
        // NavController navController = navHostFragment.getNavController();
        // NavigationUI.setupWithNavController(navView, navController);

        // This will be implemented after creating the navigation resources
        // BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // NavigationUI.setupWithNavController(navView, navController);
