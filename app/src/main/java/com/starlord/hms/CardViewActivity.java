package com.starlord.hms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class CardViewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    AppDatabase db = new AppDatabase(this);
    String checkoutDate,checkInDate;
    ProgressDialog progressDialog;
    ArrayList<DataObject> results = new ArrayList<>();
    Dialog myDialog;
    int pos;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        toolbar = findViewById(R.id.cardViewToolbar);
        toolbar.setTitle("Available Rooms");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Payment proceeding...");
        Intent intent = getIntent();
        checkInDate = intent.getStringExtra("checkInDate");
        checkoutDate = intent.getStringExtra("checkoutDate");
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        myDialog = new Dialog(this);
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
        finish();

    }

    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                pos = position;
                ShowPopup();
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        results = db.searchRoom(checkInDate,checkoutDate);
        return results;
    }

    public void ShowPopup() {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.popup_layout);

        txtclose = myDialog.findViewById(R.id.txtclose);
        TextView roomname = myDialog.findViewById(R.id.roomname);
        TextView available = myDialog.findViewById(R.id.available);
        TextView capacity = myDialog.findViewById(R.id.capacity);
        TextView price = myDialog.findViewById(R.id.price);

        roomname.setText(results.get(pos).getmText1());
        available.setText(results.get(pos).getmText2());
        capacity.setText(results.get(pos).getmText3());
        price.setText(results.get(pos).getmText4());
        btnFollow = myDialog.findViewById(R.id.btnfollow);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnFollow.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int n = db.maxreservesid();
                        int roomnumber;
                        int roomtype = Integer.valueOf(results.get(pos).getmText5());
                        roomnumber = db.availableroom(roomtype,checkInDate,checkoutDate);
                        db.insertReserves(n+1,checkInDate,checkoutDate,Integer.valueOf(results.get(pos).getmText3()),"reserved",roomnumber,1);
                        Intent intent = new Intent(getApplicationContext(),BookingActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                        finish();
                    }
                },2000);
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
