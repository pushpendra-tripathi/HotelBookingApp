package com.starlord.hms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

public class NavigationActivity extends AppCompatActivity {
    Button search, booking, logOut;
    Toolbar toolbar;
    TextView profileName;
    AppDatabase db = new AppDatabase(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        toolbar = findViewById(R.id.navigationToolbar);
        toolbar.setTitle("Hotel Management System");
        setSupportActionBar(toolbar);

        search = findViewById(R.id.btnSearchRoom);
        booking = findViewById(R.id.btnBookings);
        logOut = findViewById(R.id.btnLogOut);
        profileName = findViewById(R.id.txtProfileName);

        profileName.setText("Welcome "+db.customerName());

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BookingActivity.class));
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.logOutSession();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

    }
}
