package com.starlord.hms;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter1 extends RecyclerView
        .Adapter<MyRecyclerViewAdapter1
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter1";
    private ArrayList<DataObject1> mDataset;
    private static MyClickListener1 myClickListener1;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label1;
        TextView label2;
        TextView label3;
        TextView label4;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label1 = (TextView) itemView.findViewById(R.id.Resid);
            label2 = (TextView) itemView.findViewById(R.id.checkindate);
            label3 = (TextView) itemView.findViewById(R.id.checkoutdate);
            label4 = (TextView) itemView.findViewById(R.id.roomtype);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener1.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener1(MyClickListener1 myClickListener1) {
        this.myClickListener1 = myClickListener1;
    }

    public MyRecyclerViewAdapter1(ArrayList<DataObject1> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_card_layout, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label1.setText("ResId        : "+mDataset.get(position).getmText1());
        holder.label2.setText("CheckInDate  : "+mDataset.get(position).getmText2());
        holder.label3.setText("CheckOutDate : "+mDataset.get(position).getmText3());
        holder.label4.setText("Speciality   : "+mDataset.get(position).getmText4());
    }

    public void addItem(DataObject1 dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        if(mDataset!=null)
            return mDataset.size();
        else
            return 0;
    }

    public interface MyClickListener1 {
        public void onItemClick(int position, View v);
    }
}
