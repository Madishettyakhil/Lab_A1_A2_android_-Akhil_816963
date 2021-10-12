package com.android.androidassignment;

public class Product {
    int id;
    String productname,productdesc,productprice,providername,provideremail,providerphone,
            latitude,longitude;

    public Product(int id, String productname,String productdesc,String productprice,
                   String providername,String provideremail,String providerphone,
                   String latitude,String longitude)
    {
        this.id = id;
        this.productname = productname;
        this.productdesc = productdesc;
        this.productprice = productprice;
        this.providername = providername;
        this.provideremail = provideremail;
        this.providerphone = providerphone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId()
    {
        return id;
    }

    public String getProductname() {
        return productname;
    }

    public String getProductdesc() {
        return productdesc;
    }

    public String getProductprice() {
        return productprice;
    }

    public String getProvidername() {
        return providername;
    }

    public String getProvideremail() {
        return provideremail;
    }

    public String getProviderphone() {
        return providerphone;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
