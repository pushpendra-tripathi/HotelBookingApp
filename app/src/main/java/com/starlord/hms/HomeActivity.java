package com.starlord.hms;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {
    TextView tvCheckIn,tvCheckout;
    Calendar mCurrentDate;
    int day,month,year;
    int checkIn_day, checkIn_month, checkIn_year, checkout_day, checkout_month, checkout_year;
    Button search_Button;
    ProgressDialog progressDialog;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.homeToolbar);
        toolbar.setTitle("Search Hotel");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Searching...");
        search_Button = findViewById(R.id.btn_search);
        tvCheckIn = (TextView) findViewById(R.id.btn_checkIn);
        tvCheckout = (TextView) findViewById(R.id.btn_checkout);
        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        int nextDay = day + 1;
        month = month + 1;
        tvCheckIn.setText(day+"/"+month+"/"+year);
        tvCheckout.setText(day+"/"+month+"/"+year);

        tvCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //monthOfYear = monthOfYear+1;
                        tvCheckIn.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                        checkIn_day = dayOfMonth;
                        checkIn_month = monthOfYear;
                        checkIn_year = year;
                    }
                },year,month,day);
                datePicker.show();
            }
        });

        tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //dayOfMonth = dayOfMonth+1;
                        tvCheckout.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                        checkout_day = dayOfMonth;
                        checkout_month = monthOfYear;
                        checkout_year = year;
                    }
                },year,month,day);
                datePicker.show();
            }
        });

        search_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    public void validate(){
        if(checkout_day>checkIn_day || checkout_month > checkIn_month || checkout_year > checkIn_year){
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    Intent intent = new Intent(getApplicationContext(),CardViewActivity.class);
                    String CheckInDate = checkIn_year+"-"+checkIn_month+"-"+checkIn_day;
                    String CheckoutDate = checkout_year+"-"+checkout_month+"-"+checkout_day;
                    intent.putExtra("checkInDate",CheckInDate);
                    intent.putExtra("checkoutDate",CheckoutDate);
                    progressDialog.dismiss();
                    startActivity(intent);
                    finish();
                }
            },2000);
        }
        else{
            Toast.makeText(getBaseContext(), "Incorrect Dates", Toast.LENGTH_LONG).show();
        }
    }

}
