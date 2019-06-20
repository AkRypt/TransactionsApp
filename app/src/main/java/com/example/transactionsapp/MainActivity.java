package com.example.transactionsapp;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // For Authentication
    private static final int RC_SIGN_IN = 1;
    // For Shared Preferences
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    // Firebase Authentication
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For Bottom Navigation and Fragments
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Entry()).commit();
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFrag = null;
                switch (menuItem.getItemId()) {
                    case R.id.entry:
                        selectedFrag = new Entry();
                        break;
                    case R.id.transactions:
                        selectedFrag = new Transactions();
                        break;
                    case R.id.about:
                        selectedFrag = new Info();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFrag).commit();
                return true;
            }
        });


        // For Authentication - Checks if user is authenticated or not
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser userx = firebaseAuth.getCurrentUser();

                if (userx != null) {
                    Toast.makeText(MainActivity.this, "You're now logged in!", Toast.LENGTH_SHORT).show();
                    user = mAuth.getUid();
                    // Sending user uid to all Activities
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("user", user);
                    editor.apply();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Entry()).commit();
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
}
