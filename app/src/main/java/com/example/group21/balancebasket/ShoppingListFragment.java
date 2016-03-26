package com.example.group21.balancebasket;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment {

    private ListView listView;
    private SQLiteDatabase sqLiteDatabase;
    private UserDBHelper userDbHelper;
    private Cursor cursor;
    private ListDataAdapter listDataAdapter;
    private DataProvider dataProvider;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        listView = (ListView) view.findViewById(R.id.ListView);
        listDataAdapter = new ListDataAdapter(this.getContext(), R.layout.row_layout);
        provideData();
        listView.setAdapter(listDataAdapter);
        return view;
    }

    private void provideData() {
        dataProvider = new DataProvider();
        userDbHelper = new UserDBHelper(this.getContext());
        sqLiteDatabase = userDbHelper.getReadableDatabase();
        cursor = dataProvider.query(null, null, null, null, null);// UserDBHelper.getProducts(sqLiteDatabase);

//        if (cursor.moveToFirst()) {
//            do {

//        String name, price;
//                name = cursor.getString(0);
//                price = cursor.getString(1);
//        dataProvider = new DataProvider(name, price);
        // TODO: 26-3-2016 replace by shopping list from database
        // create list of products to show on page
        List<Product> products = new ArrayList<>();
        // create products
        Product apple = new Product("apple", "0,25");
        Product banana = new Product("banana", "0,15");
        Product toothpaste = new Product("toothpaste", "1,25");
        Product cookies = new Product("cookies", "3,75");
        // add products to list
        products.add(apple);
        products.add(banana);
        products.add(toothpaste);
        products.add(cookies);

        // add products from list to dataprovider
        for (Product product:products
             ) {
            dataProvider = new DataProvider(product.getName(), product.getPrice());
            listDataAdapter.add(dataProvider);
        }

//            } while (cursor.moveToNext());
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
