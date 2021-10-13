package com.android.androidassignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EntryActivity extends AppCompatActivity {

    EditText product_id,product_name,product_desc,product_price,provider_name,
            provider_email,provider_phone;

    Button save,location;

    TextView error_message;

    Double latitude,longitude;

    String activity;

    ProductDBHelper dbHelper;

    int id;

    Product product;

    ActivityResultLauncher<Intent> getresult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        assert data != null;
                        latitude = data.getDoubleExtra("latitude",0);
                        longitude = data.getDoubleExtra("longitude",0);
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        getSupportActionBar().setTitle("Enter Product");

        activity = getIntent().getStringExtra("activity");
        product_id = findViewById(R.id.product_id);
        product_name = findViewById(R.id.product_name);
        product_desc = findViewById(R.id.product_desc);
        product_price = findViewById(R.id.product_price);
        provider_name = findViewById(R.id.provider_name);
        provider_email = findViewById(R.id.provider_email);
        provider_phone = findViewById(R.id.provider_phone);

        error_message = findViewById(R.id.error_message);

        save = findViewById(R.id.save);
        location = findViewById(R.id.location);

        dbHelper = new ProductDBHelper(this);
        dbHelper.openDataBase();
        ArrayList<Integer> pid = dbHelper.getid();

        if(activity.equals("viewing"))
        {
            id = getIntent().getIntExtra("id",1);
            product = dbHelper.getProductbyID(id);
            product_id.setFocusable(false);
            product_id.setText(String.valueOf(product.getId()));
            product_name.setText(String.valueOf(product.getProductname()));
            product_desc.setText(String.valueOf(product.getProductdesc()));
            product_price.setText(String.valueOf(product.getProductprice()));
            provider_name.setText(String.valueOf(product.getProvidername()));
            provider_email.setText(String.valueOf(product.getProvideremail()));
            provider_phone.setText(String.valueOf(product.getProviderphone()));
            latitude = Double.valueOf(String.valueOf(product.getLatitude()));
            longitude = Double.valueOf(String.valueOf(product.getLongitude()));
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntryActivity.this,MapsActivity.class);
                intent.putExtra("activity","entry");
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                getresult.launch(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = product_id.getText().toString();
                String name,desc,price,provider,email,phone;
                name = product_name.getText().toString();
                desc = product_desc.getText().toString();
                price = product_price.getText().toString();
                provider = provider_name.getText().toString().toLowerCase();
                email = provider_email.getText().toString();
                phone = provider_phone.getText().toString();

                if(id.equals(""))
                {
                    product_id.setError("please give an id");
                }
                else if(name.equals(""))
                {
                    product_name.setError("please enter a name");
                }
                else if(desc.equals(""))
                {
                    product_desc.setError("descreption cannot be empty");
                }
                else if(price.equals(""))
                {
                    product_price.setError("price cannot be empty");
                }
                else if(provider.equals(""))
                {
                    provider_name.setError("there should be a provider name");
                }
                else if(email.equals(""))
                {
                    provider_email.setError("email cannot be blank");
                }
                else if (phone.equals(""))
                {
                    provider_phone.setError("phone cannot be blank");
                }
                else if(latitude == null || longitude == null)
                {
                    error_message.setText("Please enter provider's location");
                }
                else if(pid.contains(Integer.parseInt(id)) && activity.equals("main"))
                {
                    product_id.setError("Id already exists, use a different one");
                }
                else
                {
                    if(activity.equals("main"))
                    {
                        dbHelper.insertProduct(Integer.valueOf(id),name,desc,price,provider,email,phone,
                                String.valueOf(latitude),String.valueOf(longitude));
                        finish();
                    }
                    else if(activity.equals("viewing"))
                    {
                        dbHelper.UpdateProduct(Integer.valueOf(id),name,desc,price,provider,email,phone,
                                String.valueOf(latitude),String.valueOf(longitude));
                        finish();
                    }
                }
            }
        });

        dbHelper.close();
    }




@Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}