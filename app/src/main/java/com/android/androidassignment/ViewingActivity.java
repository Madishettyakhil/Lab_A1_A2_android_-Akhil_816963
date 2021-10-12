package com.android.androidassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewingActivity extends AppCompatActivity {

    ListView listView;

    ProductDBHelper dbHelper;
    Product product;
    String[] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing);

        listView = findViewById(R.id.listview);

        int id = getIntent().getIntExtra("id",1);

        dbHelper = new ProductDBHelper(this);
        dbHelper.openDataBase();

        product = dbHelper.getProductbyID(id);

        dbHelper.close();

        data = new String[]{"Product Id :"+product.getId(),"Product Name :"+product.getProductname(),
                "Product Desc :"+product.getProductdesc(),"Product Price :"+product.getProductprice(),
                "Provider Name :"+product.getProvidername(),"Provider Email :"+product.getProvideremail(),
                "Provider Phone :"+product.getProviderphone(),"Latitude :"+product.getLatitude(),
                "Longitude :"+product.getLongitude()};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);

        listView.setAdapter(adapter);

        getSupportActionBar().setTitle(product.getProductname());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}