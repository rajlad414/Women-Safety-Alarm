package com.example.womensafetyalaramapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="mylist_db" ;
    public static final String TABLE_NAME="mylist_data";
    public static final String COL1="ID";
    public static final String COL2="Phone";


    DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable= "create table "+TABLE_NAME+" (ID integer primary key autoincrement, Phone text)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = "Drop table if exists "+TABLE_NAME;
        db.execSQL(drop);
        onCreate(db);
    }

    public boolean addData(String phone){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL2,phone);
        long result= db.insert(TABLE_NAME,null,contentValues);
        if(result==-1) return false;
        else return true;
    }

    public Cursor getListContents(){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor data=db.rawQuery("select * from "+TABLE_NAME,null);
        return data;
    }

    public boolean DeleteData(String number) {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(DatabaseHandler.TABLE_NAME,
                DatabaseHandler.COL2+"=?",
                new String[]{number})>0;
    }
}
