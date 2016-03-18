package com.example.group21.balancebasket;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class DataListActivity extends Fragment {

    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    UserDbHelper userDbHelper;
    Cursor cursor;
    ListDataAdapter listDataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_list_layout);
        listView = (ListView) findViewById(R.id.ListView);
        listDataAdapter = new ListDataAdapter(getApplicationContext(), R.layout.row_layout);
        listView.setAdapter(listDataAdapter);

        userDbHelper = new UserDbHelper(getApplicationContext());
        sqLiteDatabase = userDbHelper.getReadableDatabase();
        cursor = UserDbHelper.getProducts(sqLiteDatabase);

        if (cursor.moveToFirst()) {
            do {

                String name, price;
                name = cursor.getString(0);
                price = cursor.getString(1);
                DataProvider dataProvider = new DataProvider(name, price);
                listDataAdapter.add(dataProvider);


            } while (cursor.moveToNext());
        }

    }
}