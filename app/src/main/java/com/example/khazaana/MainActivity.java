package com.example.khazaana;

import static androidx.navigation.Navigation.findNavController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ifa_home()).commit();
        bottomNavigationView.setSelectedItemId(R.id.ifa_home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag = null;
                switch(item.getItemId()) {
                    case R.id.ifa_home:
                        frag = new ifa_home();
                        break;
                    case R.id.client_list:
                        frag = new client_list();
                        break;
                    case R.id.settings2:
                        frag = new settings();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, frag).commit();
                return true;
            }
        });
    }
}