package com.example.group21.balancebasket;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DataListActivity extends Fragment {

    private ListView listView;
    private SQLiteDatabase sqLiteDatabase;
    private UserDBHelper userDbHelper;
    private Cursor cursor;
    private ListDataAdapter listDataAdapter;
    private DataProvider dataProvider;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.data_list_layout);
//        listView = (ListView) findViewById(R.id.ListView);
        listDataAdapter = new ListDataAdapter(this.getContext(), R.layout.row_layout);
        listView.setAdapter(listDataAdapter);

        userDbHelper = new UserDBHelper(this.getContext());
        sqLiteDatabase = userDbHelper.getReadableDatabase();
        cursor = dataProvider.query(null, null, null, null, null);// UserDBHelper.getProducts(sqLiteDatabase);

        if (cursor.moveToFirst()) {
            do {

                String name, price;
                name = cursor.getString(0);
                price = cursor.getString(1);
//                DataProvider dataProvider = new DataProvider(name, price);
                listDataAdapter.add(dataProvider);


            } while (cursor.moveToNext());
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.data_list_layout, container, false);
        listView = (ListView) view.findViewById(R.id.ListView);
        return view;
    }
}