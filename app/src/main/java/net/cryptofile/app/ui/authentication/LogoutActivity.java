package net.cryptofile.app.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.cryptofile.app.MainActivity;
import net.cryptofile.app.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LogoutActivity extends AppCompatActivity {

    private Button cancelBtn;
    private Button confirmBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //View view = getLayoutInflater().inflate(R.layout.activity_logout, null, false);
        setContentView(R.layout.activity_logout);


        cancelBtn = (Button) findViewById(R.id.cancel);
        confirmBtn = (Button) findViewById(R.id.confirm);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogoutActivity.this, MainActivity.class));
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogoutActivity.this, LoginActivity.class));
            }
        });


        //setContentView(R.layout.activity_login);
        //startActivity(new Intent(this,LoginActivity.class));
    }

}
