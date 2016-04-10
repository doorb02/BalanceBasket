package com.example.group21.balancebasket;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText Prname;
    private EditText Prprice;
    private Button AddButton;
    private Button RemoveButton;
    private TextView TotalPrice;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        listView = (ListView) view.findViewById(R.id.ListView);
        TotalPrice = (TextView) view.findViewById(R.id.TotalPriceView);
        Prname = (EditText) view.findViewById(R.id.NameEditText);
        Prprice = (EditText) view.findViewById(R.id.PriceEditText);
        AddButton = (Button) view.findViewById(R.id.AddButton);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "";
                Double price = 0.0;
                // secure code against invalid input
                try{
                    name = Prname.getText().toString();
                    price = Double.valueOf(Prprice.getText().toString());
                } catch(Exception e) {
                    Toast.makeText(getActivity(), "Nothing to add", Toast.LENGTH_SHORT).show();
                }

                // add product only when name and price are entered
                if((name == "" || price == 0.0 )){
                    Toast.makeText(getActivity(), "Nothing to add", Toast.LENGTH_SHORT).show();
                }else {
                    userDbHelper.addProduct(sqLiteDatabase, name, price) ;
                    // refresh values in shopping list table
                    provideData();
                    // clear input fields
                    Prname.setText("");
                    Prprice.setText("");
    //              DBHelper = new UserDBHelper(getContext());
    //              db = DBHelper.getWritableDatabase();
    //
    //              String totalp = userDbHelper.calculateTotalPrice(db).total.getString;
    //              TotalPrice.setText(totalP); //TODO Get the program to print the total price in the textView TotalPrice
                    Toast.makeText(getActivity(), name + " added to shoppinglist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RemoveButton = (Button) view.findViewById(R.id.RemoveButton);
        RemoveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                String name = Prname.getText().toString();

                if((name.length() == 0)){
                    Toast.makeText(getActivity(), "Nothing to remove", Toast.LENGTH_SHORT).show();
                }

                else{
                    boolean isProductRemoved = userDbHelper.removeProduct(name);
                    if(isProductRemoved) {
                        provideData();
                        Toast.makeText(getActivity(), name + " removed from shoppinglist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), name + " is not removed from shoppinglist", Toast.LENGTH_SHORT).show();
                    }

                Prname.setText("");
                Prprice.setText("");
             //   userDbHelper.calculateTotalPrice();
            }}

        });
        provideData();
        return view;
    }

    // get products from the database and add them to the shopping list
    private void provideData() {
        if(dataProvider == null || userDbHelper == null || sqLiteDatabase == null) {
            dataProvider = new DataProvider();
            // init SQLiteOpenHelper
            userDbHelper = new UserDBHelper(this.getContext());
            // get a readable and writable database
            sqLiteDatabase = userDbHelper.getWritableDatabase();
        }
        // get products from the database
        List<Product> products = userDbHelper.getProducts(sqLiteDatabase);

        // empty listDataAdapter before filling adapter with new values from database
        listDataAdapter = new ListDataAdapter(this.getContext(), R.layout.row_layout);
        listView.setAdapter(listDataAdapter);

        // add products from list to dataprovider
        for (Product product:products) {
            dataProvider = new DataProvider(product.getName(), product.getPrice());
            listDataAdapter.add(dataProvider);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.follow_toggle, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


}
