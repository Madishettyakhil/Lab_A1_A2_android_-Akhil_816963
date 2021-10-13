package com.android.androidassignment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewProductActivity extends AppCompatActivity {

    String provider;

    ProductDBHelper dbHelper;

    SearchView searchView;
    RecyclerView recyclerView;

    ArrayList<String> providerArrayList = new ArrayList<String>();
    ArrayList<Integer> id = new ArrayList<>();
    ArrayList<String> productname = new ArrayList<String>();
    ArrayList<String> productprice = new ArrayList<String>();

    ArrayList<Integer> finalid = new ArrayList<>();
    ArrayList<String> finalproduct = new ArrayList<>();
    ArrayList<String> finalprice = new ArrayList<>();

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        provider = getIntent().getStringExtra("provider");
        getSupportActionBar().setTitle(provider);

        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.product_view);

        dbHelper = new ProductDBHelper(this);
        providerArrayList = dbHelper.getProvider();
        id = dbHelper.getid();
        productname = dbHelper.getProduct();
        productprice = dbHelper.getProductPrice();

        for(int i =0;i < providerArrayList.size();i++)
        {
            if(providerArrayList.get(i).equals(provider))
            {
                finalid.add(id.get(i));
                finalproduct.add(productname.get(i));
                finalprice.add(productprice.get(i));
            }
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        ProductRecyclerviewAdapter productRecyclerviewAdapter = new
                ProductRecyclerviewAdapter(finalid,finalproduct,finalprice);
        recyclerView.setAdapter(productRecyclerviewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productRecyclerviewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productRecyclerviewAdapter.getFilter().filter(newText);
                return false;
            }
        });

        ItemTouchHelper.SimpleCallback itsc = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                final int index = viewHolder.getAdapterPosition();

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(ViewProductActivity.this);
                alertdialog.setTitle("Delete Item");
                alertdialog.setMessage("Clicking on yes will delete the item");
                alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteProductbyid(Integer.parseInt(String.valueOf(finalid.get(index))));
                        finalid.remove(index);
                        finalproduct.remove(index);
                        finalprice.remove(index);
                        productRecyclerviewAdapter.notifyItemRemoved(index);
                    }
                });
                alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        productRecyclerviewAdapter.notifyDataSetChanged();
                    }
                });
                alertdialog.create().show();

            }
        };

        ItemTouchHelper ith = new ItemTouchHelper(itsc);
        ith.attachToRecyclerView(recyclerView);

        dbHelper.close();
    }
}