package com.example.group21.balancebasket;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    private EditText name;
    private EditText price;
    private Button AddButton;
    private Button RemoveButton;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        listView = (ListView) view.findViewById(R.id.ListView);
        name = (EditText) view.findViewById(R.id.NameEditText);
        price = (EditText) view.findViewById(R.id.PriceEditText);
        AddButton = (Button) view.findViewById(R.id.AddButton);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "AddButtonClicked", Toast.LENGTH_LONG).show();
            }
        });
        RemoveButton = (Button) view.findViewById(R.id.RemoveButton);
        listDataAdapter = new ListDataAdapter(this.getContext(), R.layout.row_layout);
        provideData();
        listView.setAdapter(listDataAdapter);
        return view;




    }

    // get products from the database and add them to the shopping list
    private void provideData() {
        dataProvider = new DataProvider();
        // init SQLiteOpenHelper
        userDbHelper = new UserDBHelper(this.getContext());
        // get a readable and writable database
        sqLiteDatabase = userDbHelper.getWritableDatabase();

        // get products from the database
        List<Product> products = userDbHelper.getProducts(sqLiteDatabase);

        // add products from list to dataprovider
        for (Product product:products
                ) {
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
