package com.starlord.hms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class BookingActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    ProgressDialog progressDialog;
    private RecyclerView.LayoutManager mLayoutManager;
    AppDatabase db = new AppDatabase(this);
    ArrayList<DataObject1> results = new ArrayList<>();
    Dialog myDialog;
    int pos;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        toolbar = findViewById(R.id.bookingToolbar);
        toolbar.setTitle("My Bookings");
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.my_recycler_view1);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter1(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        myDialog = new Dialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Cancelling ...");

    }

    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter1) mAdapter).setOnItemClickListener1(new MyRecyclerViewAdapter1
                .MyClickListener1() {
            @Override
            public void onItemClick(int position, View v) {
                pos = position;
                ShowPopup1();
            }
        });
    }

    private ArrayList<DataObject1> getDataSet() {
        int a = db.numberOfColumns("reserves");
        if(a==0){
            Toast.makeText(getBaseContext(), "No bookings to show", Toast.LENGTH_LONG).show();
            return null;
        }
        else {
            results = db.bookingHistory();
            return results;
        }
    }

    public void ShowPopup1() {
        TextView xclose;
        Button btncancel;
        myDialog.setContentView(R.layout.popup_layout_1);

        xclose = myDialog.findViewById(R.id.xclose);
        TextView roomname1 = myDialog.findViewById(R.id.roomname1);
        TextView checkin = myDialog.findViewById(R.id.checkin);
        TextView resid = myDialog.findViewById(R.id.reservationid);
        TextView checkout = myDialog.findViewById(R.id.checkout);
        roomname1.setText(results.get(pos).getmText4());
        checkin.setText(results.get(pos).getmText2());
        resid.setText(results.get(pos).getmText1());
        checkout.setText(results.get(pos).getmText3());
        btncancel = (Button) myDialog.findViewById(R.id.btncancel);

        xclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        db.cancelreservation(Integer.valueOf(results.get(pos).getmText1()));
                        Intent intent = new Intent(getApplicationContext(),BookingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },2000);
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
