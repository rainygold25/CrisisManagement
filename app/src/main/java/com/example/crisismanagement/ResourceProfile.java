package com.example.crisismanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ResourceProfile extends AppCompatActivity {

    String name;
    String address;
    String contact_information;
    String geo_location;
    String image;
    String date;
    String[] items;
    String[] quantities;
    String[] categories;

    ImageView imageView;
    TextView textView_name;
    TextView textView_address;
    TextView textView_contact_information;
    ImageView button_call;
    ImageView button_locate;
    NonScrollListView listView_items;
    NonScrollListView listView_categories;

    String location_str = "http://maps.google.com?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_profile);

        imageView = (ImageView) findViewById(R.id.imageView_PhotoSlot);
        textView_name = (TextView) findViewById(R.id.editText_Name);
        textView_address = (TextView) findViewById(R.id.editText_Address);
        textView_contact_information = (TextView) findViewById(R.id.editText_ContactInfo);
        button_call = (ImageView) findViewById(R.id.imageView3);
        button_locate = (ImageView) findViewById(R.id.imageView2);
        listView_categories = (NonScrollListView) findViewById(R.id.listCategories);
        listView_items = (NonScrollListView) findViewById(R.id.linearLayout_itemsList);

        listView_items.setScrollContainer(false);
        listView_categories.setScrollContainer(false);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        contact_information = intent.getStringExtra("contact");
        geo_location = intent.getStringExtra("geo_location");
        image = intent.getStringExtra("image");
        date = intent.getStringExtra("date");
        items = intent.getStringArrayExtra("items");
        quantities = intent.getStringArrayExtra("quantities");
        categories = intent.getStringArrayExtra("categories");

        textView_name.setText(name);
        imageView.setImageBitmap(convertStringToBitmap(image));
        textView_address.setText(address);
        textView_contact_information.setText(contact_information);
        ArrayAdapter<String> categories_lst_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        listView_categories.setAdapter(categories_lst_adapter);
        String[] display_lst_items = new String[items.length];
        for (int i = 0; i < items.length; i += 1) {
            display_lst_items[i] = quantities[i] + " " + items[i];
        }
        ArrayAdapter<String> items_lst_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, display_lst_items);
        listView_items.setAdapter(items_lst_adapter);

        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ResourceProfile.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ResourceProfile.this, new String[] {android.Manifest.permission.CALL_PHONE},
                            10);
                    if (ContextCompat.checkSelfPermission(ResourceProfile.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact_information));
                        startActivity(intent);
                    }
                } else {

// else block means user has already accepted.And make your phone call here.
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact_information));
                    startActivity(intent);
                }

            }
        });

        button_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] lat_long_str = new String[2];
                lat_long_str = geo_location.split(" ");
                String intent_url = location_str + lat_long_str[0] + "," + lat_long_str[1];
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(intent_url));
                startActivity(i);
            }
        });
    }

    public static Bitmap convertStringToBitmap(String string) {
        byte[] byteArray1;
        byteArray1 = Base64.decode(string, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray1, 0,
                byteArray1.length);/* w  w  w.ja va 2 s  .  c om*/
        return bmp;
    }
}