package com.starlord.hms;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class AppDatabase extends SQLiteOpenHelper {
    private static final int dbv = 1;
    private static final String dbname = "hms.db";
    Context ct;
    public AppDatabase(Context context) {
        super(context, dbname, null, dbv);
        ct = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryUserTable = "CREATE TABLE Customer(email_id VARCHAR(40) NOT NULL,customer_id INT,username VARCHAR(20) NOT NULL,password VARCHAR(20) NOT NULL,session INT,PRIMARY KEY (customer_id))";
        String queryRoomTypeTable = "CREATE TABLE Room_Type(type_name VARCHAR(20) NOT NULL,description VARCHAR(100) NOT NULL,room_price INT NOT NULL,total_rooms INT NOT NULL,type_id INT,capacity INT NOT NULL,no_of_rooms_available INT NOT NULL,PRIMARY KEY (type_id))";
        String queryRoomTable = "CREATE TABLE Room(room_number INT NOT NULL,type_id INT NOT NULL,PRIMARY KEY (room_number),FOREIGN KEY (type_id) REFERENCES Room_Type(type_id))";
        String queryReservesTable = "CREATE TABLE reserves(reservation_id INT,arrival_date VARCHAR NOT NULL,departure_date VARCHAR NOT NULL,no_of_guests INT NOT NULL,status VARCHAR(20) NOT NULL,room_number INT NOT NULL,customer_id INT NOT NULL,PRIMARY KEY (reservation_id),FOREIGN KEY (room_number) REFERENCES Room(room_number),FOREIGN KEY (customer_id) REFERENCES Customer(customer_id))";

        String query1 = "INSERT INTO Room_Type VALUES('DELUXE SINGLE','These Deluxe Rooms let you relax as you admire a beautiful view of the pool.',1000,4,1,1,4)";
        String query2 = "INSERT INTO Room_Type VALUES('DELUXE DOUBLE', 'These Deluxe Rooms let you relax as you admire a beautiful view of the pool.', 1800, 2, 2, 2, 2)";
        String query3 = "INSERT INTO Room_Type VALUES('DELUXE TRIPLE', 'These Deluxe Rooms let you relax as you admire a beautiful view of the pool.', 2400, 1, 3, 3, 1)";
        String query4 = "INSERT INTO Room VALUES(1, 1)";
        String query5 = "INSERT INTO Room VALUES(2, 1)";
        String query6 = "INSERT INTO Room VALUES(3, 1)";
        String query7 = "INSERT INTO Room VALUES(4, 1)";
        String query8 = "INSERT INTO Room VALUES(5, 2)";
        String query9 = "INSERT INTO Room VALUES(6, 2)";
        String query10 = "INSERT INTO Room VALUES(7, 3)";

        sqLiteDatabase.execSQL(queryUserTable);
        sqLiteDatabase.execSQL(queryRoomTypeTable);
        sqLiteDatabase.execSQL(queryRoomTable);
        sqLiteDatabase.execSQL(queryReservesTable);
        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query2);
        sqLiteDatabase.execSQL(query3);
        sqLiteDatabase.execSQL(query4);
        sqLiteDatabase.execSQL(query5);
        sqLiteDatabase.execSQL(query6);
        sqLiteDatabase.execSQL(query7);
        sqLiteDatabase.execSQL(query8);
        sqLiteDatabase.execSQL(query9);
        sqLiteDatabase.execSQL(query10);
        Toast.makeText(ct, "All queries executed", Toast.LENGTH_SHORT).show();
    }

    public int searchUser(String email,String password){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM Customer where email_id = '"+email+"' and password = '"+password+"';";
        Log.d("searchUser_query",query);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor!=null && cursor.getCount()>0){
            return cursor.getCount();
        }
        return 0;
    }

    public int searchSession(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM Customer where session==1";
        Cursor cursor = db.rawQuery(query,null);
        if (cursor!=null && cursor.getCount()>0){
            return 1;
        }
        return 0;
    }

    public void logOutSession(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM Customer where session==1";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            String email = cursor.getString(0);
            int id = cursor.getInt(1);
            String userName = cursor.getString(2);
            String password = cursor.getString(3);
            int session = 0;
            ContentValues cv = new ContentValues();
            cv.put("email_id",email);
            cv.put("customer_id",id);
            cv.put("userName",userName);
            cv.put("password",password);
            cv.put("session",session);
            db.update("Customer", cv, "customer_id ="+id,null);
        }
    }

    public void loginSession(String email, String password){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM Customer where email_id = '"+email+"' and password = '"+password+"';";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(1);
            String userName = cursor.getString(2);
            int session = 1;
            ContentValues cv = new ContentValues();
            cv.put("email_id",email);
            cv.put("customer_id",id);
            cv.put("userName",userName);
            cv.put("password",password);
            cv.put("session",session);
            db.update("Customer", cv, "customer_id ="+id,null);
        }
    }

    public int numberOfColumns(String value){
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select * from "+value+";";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0){
            return cursor.getCount();
        }
        return 0;
    }

    public void insertCustomer(String email_id, int customer_id, String userName, String password, int session ){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email_id",email_id);
        contentValues.put("customer_id",customer_id);
        contentValues.put("userName",userName);
        contentValues.put("password",password);
        contentValues.put("session",session);
        db.insert("Customer",null,contentValues);
    }


    public ArrayList<DataObject> searchRoom(String checkInDate, String checkoutDate){
        String query = "SELECT type_name,room_price, capacity,COUNT(room_number),Room_Type.type_id from Room_Type,(SELECT room_number, type_id from Room where room_number not in(select room_number from reserves NATURAL JOIN Room WHERE reserves.status not LIKE 'cancelled' and '"+checkoutDate+"' >= reserves.arrival_date and '"+checkInDate+"' <= reserves.departure_date))AS S where Room_Type.type_id = S.type_id GROUP BY type_name, description ,room_price, capacity,Room_Type.type_id HAVING COUNT(room_number)>0;";
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DataObject> str = new ArrayList<>();
        int index=0;
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst()){
            do {
                String column1 = c.getString(0);
                String column2 = c.getString(1);
                String column3 = c.getString(2);
                String column4 = c.getString(3);
                String column5 = c.getString(4);
                DataObject obj = new DataObject(column1,column2,column3,column4,column5);
                str.add(index,obj);
                index=index+1;
            } while(c.moveToNext());
        }
        c.close();
        return str;
    }

    public int maxreservesid( ){
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select reservation_id from reserves";
        int max =0;
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst()){
            do {
                String column1 = c.getString(0);
                if(max < Integer.valueOf(column1))
                    max = Integer.valueOf(column1);
            } while(c.moveToNext());
        }
        c.close();
        return max;
    }

    public int availableroom(int roomtype,String checkindate,String checkoutdate){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT room_number from Room where room_number not in (select room_number from reserves NATURAL JOIN Room" +
                " WHERE reserves.status not LIKE 'cancelled' and '"+checkoutdate+"'>= reserves.arrival_date " +
                "and '"+checkindate+"'<= reserves.departure_date) AND type_id = "+roomtype+";";
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        return Integer.valueOf(cursor.getString(0));
    }

    public void insertReserves(int reservation_id,String arrival_date,String departure_date,int no_of_guests,String status,int room_number,int customer_id){
        SQLiteDatabase db = getWritableDatabase();
        String insertreserves = "INSERT INTO reserves values("+reservation_id+",'"+arrival_date+"','"+departure_date+"',"+no_of_guests+"," +
                "'"+status+"',"+room_number+","+customer_id+");";
        db.execSQL(insertreserves);
    }

    public  ArrayList<DataObject1> bookingHistory(){
        String query = "select reservation_id,arrival_date,departure_date,type_name from reserves,Room,Room_type where reserves.room_number = Room.room_number and Room.type_id = Room_type.type_id;";
        Log.d("searchroom_query",query);
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DataObject1> str = new ArrayList<>();
        int index=0;
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst()){
            do {
                String column1 = c.getString(0);
                String column2 = c.getString(1);
                String column3 = c.getString(2);
                String column4 = c.getString(3);
                DataObject1 obj = new DataObject1(column1,column2,column3,column4);
                str.add(index,obj);
                index=index+1;
            } while(c.moveToNext());
        }
        c.close();
        return str;
    }

    public String customerName(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT username FROM Customer where session==1";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String name = cursor.getString(0);
        return name;
    }

    public void cancelreservation(int a){
        String query = "DELETE FROM reserves where reservation_id = "+a+";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        return;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldValue, int newValue) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Customer;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Room_Type;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Room;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS reserves;");
        onCreate(sqLiteDatabase);
    }
}

