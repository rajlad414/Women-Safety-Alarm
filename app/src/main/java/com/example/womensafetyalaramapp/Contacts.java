package com.example.womensafetyalaramapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.ListResourceBundle;

public class Contacts extends AppCompatActivity {

    Button btnAdd, btnDelte,btnView;
    EditText etPhone;
    ListView list;
    SQLiteDatabase sqLitedb;
    DatabaseHandler myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        btnAdd=findViewById(R.id.btnAdd);
        btnDelte=findViewById(R.id.btnDelte);
        btnView=findViewById(R.id.btnView);
        etPhone=findViewById(R.id.etPhone);

        list=findViewById(R.id.list);
        myDB=new DatabaseHandler(this);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=etPhone.getText().toString().trim();
                boolean result=myDB.addData(number);
                if(result) Toast.makeText(
                        Contacts.this,
                        "Phone Number added Successfully",
                        Toast.LENGTH_LONG).show();
                else Toast.makeText(
                        Contacts.this,
                        "Unable to add Number",
                        Toast.LENGTH_LONG).show();
            }
        });

        btnDelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=etPhone.getText().toString();
                myDB.DeleteData(number);
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    private void loadData() {
        ArrayList<String> lists = new ArrayList<>();
        Cursor data=myDB.getListContents();
        if(data.getCount()==0){
            Toast.makeText(
                    Contacts.this,
                    "No contacts available. Please add a Contact Number.",
                    Toast.LENGTH_LONG).show();
        }
        else{
            while(data.moveToNext()){
                lists.add(data.getString(1));
                ListAdapter listAdapter=new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        lists);
                list.setAdapter(listAdapter);

            }
        }
    }
}