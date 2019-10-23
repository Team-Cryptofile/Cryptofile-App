package net.cryptofile.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    // TODO: 22.10.19  create 'check if logged in' function

    //SET TO FALSE IF YOU WANT TO TEST MAIN ACTIVITY
    boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(loggedIn) {
            setContentView(R.layout.activity_main);
        }
        else {
            setContentView(R.layout.activity_settings);
        }

    }
}