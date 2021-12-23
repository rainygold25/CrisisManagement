package com.example.crisismanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PostResources extends AppCompatActivity {
    LinearLayout items_list_layout;
    TextView addItem;
    int num_items;
    List<EditText> items_lst;
    List<EditText> quantities_lst;
    EditText name;
    EditText address;
    EditText contact_info;
    ImageView photo;
    CheckBox food_checkbox;
    CheckBox clothing_checkbox;
    CheckBox toiletries_checkbox;
    CheckBox cooking_checkbox;
    CheckBox utilities_checkbox;
    CheckBox rating_one_checkbox;
    CheckBox rating_two_checkbox;
    CheckBox rating_three_checkbox;
    CheckBox rating_four_checkbox;
    CheckBox rating_five_checkbox;
    FloatingActionButton postInfo;
    private DatabaseReference mDatabase;
    String str_photo;
    private LocationManager locationManager;
    private LocationListener locationListener;
    String location_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_resources);
        location_str = "";
        str_photo = "";
        mDatabase = FirebaseDatabase.getInstance().getReference();
        num_items = 1;
        items_list_layout = (LinearLayout) findViewById(R.id.linearLayout_itemsList);
        addItem = (TextView) findViewById(R.id.textView_addItem);
        name = (EditText) findViewById(R.id.editText_Name);
        address = (EditText) findViewById(R.id.editText_Address);
        contact_info = (EditText) findViewById(R.id.editText_ContactInfo);
        photo = (ImageView) findViewById(R.id.imageView_PhotoSlot);
        postInfo = (FloatingActionButton) findViewById(R.id.floatingActionButton_postInfo);

        food_checkbox = (CheckBox) findViewById(R.id.selectOptionFood);
        clothing_checkbox = (CheckBox) findViewById(R.id.selectOptionClothing);
        toiletries_checkbox = (CheckBox) findViewById(R.id.selectOptionToiletries);
        cooking_checkbox = (CheckBox) findViewById(R.id.selectOptionCooking);
        utilities_checkbox = (CheckBox) findViewById(R.id.selectOptionUtilities);

        rating_one_checkbox = (CheckBox) findViewById(R.id.selectRatingOne);
        rating_two_checkbox = (CheckBox) findViewById(R.id.selectRatingTwo);
        rating_three_checkbox = (CheckBox) findViewById(R.id.selectRatingThree);
        rating_four_checkbox = (CheckBox) findViewById(R.id.selectRatingFour);
        rating_five_checkbox = (CheckBox) findViewById(R.id.selectRatingFive);

        items_lst = new ArrayList<>();
        quantities_lst = new ArrayList<>();

        items_lst.add((EditText) findViewById(R.id.editText_item_1));
        quantities_lst.add((EditText) findViewById(R.id.editText_quantity_1));
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num_items += 1;
                Log.d("num_items", num_items + "");
                EditText editText_item = new EditText(PostResources.this);
                editText_item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                editText_item.setHint("Item " + num_items);
                EditText editText_quantity = new EditText(PostResources.this);
                editText_quantity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                editText_quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText_quantity.setHint("Quantity for Item " + num_items);
                items_list_layout.addView(editText_item);
                items_list_layout.addView(editText_quantity);
                items_lst.add(editText_item);
                quantities_lst.add(editText_quantity);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                location_str = location.getLatitude() + " " + location.getLongitude();
                Log.d("My Location", location_str);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("Well, this is annoyying", "Oh, what r u gonna do");
            if (PostResources.this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && PostResources.this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Now, Im actually mad", "MAD Mad mad");
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                //configureButton();
            } else {
                configureButton();
                // Hello Ranesh
            }

        } else {
            Log.d("Well, this is annoyying", "Oh, what r u gonna do");
            configureButton();
        }

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(view.getContext(),
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(PostResources.this,
                            android.Manifest.permission.CAMERA)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(PostResources.this,
                                new String[]{android.Manifest.permission.CAMERA},
                                0);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        postInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String device_id = Settings.Secure.getString(PostResources.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                String posting_str = "";
                for (int i = 0; i < 10; i++) {
                    char c = (char) (int) (97 + Math.random() * 25);
                    posting_str += c + "";
                }

                String name_str = name.getText().toString();
                String food_check = food_checkbox.isChecked() + "";
                String clothing_check = clothing_checkbox.isChecked() + "";
                String toiletries_check = toiletries_checkbox.isChecked() + "";
                String cooking_check = cooking_checkbox.isChecked() + "";
                String utilities_check = utilities_checkbox.isChecked() + "";
                String address_str = address.getText().toString();
                String contact_info_str = contact_info.getText().toString();
                String rating_one_check = rating_one_checkbox.isChecked() + "";
                String rating_two_check = rating_two_checkbox.isChecked() + "";
                String rating_three_check = rating_three_checkbox.isChecked() + "";
                String rating_four_check = rating_four_checkbox.isChecked() + "";
                String rating_five_check = rating_five_checkbox.isChecked() + "";

                mDatabase.child("Account").child(device_id).child(posting_str).child("Name").setValue(name_str);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Image").setValue(str_photo);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Food").setValue(food_check);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Clothing").setValue(clothing_check);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Toiletries").setValue(toiletries_check);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Cooking").setValue(cooking_check);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Utilities").setValue(utilities_check);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Address").setValue(address_str);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Contact Info").setValue(contact_info_str);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Rating One").setValue(rating_one_check);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Rating Two").setValue(rating_two_check);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Rating Three").setValue(rating_three_check);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Rating Four").setValue(rating_four_check);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Rating Five").setValue(rating_five_check);
                mDatabase.child("Account").child(device_id).child(posting_str).child("Geo Location").setValue(location_str);

                for (int i = 0; i < items_lst.size(); i += 1) {
                    String item_i_name = items_lst.get(i).getText().toString();
                    String item_i_quantity = quantities_lst.get(i).getText().toString();
                    mDatabase.child("Account").child(device_id).child(posting_str).child("Items").child("Item " + (i + 1) + " Name").setValue(item_i_name);
                    mDatabase.child("Account").child(device_id).child(posting_str).child("Items").child("Item " + (i + 1) + " Quantity").setValue(item_i_quantity);
                }

                mDatabase.child("Resources").child(posting_str).child("Name").setValue(name_str);
                mDatabase.child("Resources").child(posting_str).child("Image").setValue(str_photo);
                mDatabase.child("Resources").child(posting_str).child("Food").setValue(food_check);
                mDatabase.child("Resources").child(posting_str).child("Clothing").setValue(clothing_check);
                mDatabase.child("Resources").child(posting_str).child("Toiletries").setValue(toiletries_check);
                mDatabase.child("Resources").child(posting_str).child("Cooking").setValue(cooking_check);
                mDatabase.child("Resources").child(posting_str).child("Utilities").setValue(utilities_check);
                mDatabase.child("Resources").child(posting_str).child("Address").setValue(address_str);
                mDatabase.child("Resources").child(posting_str).child("Contact Info").setValue(contact_info_str);
                mDatabase.child("Resources").child(posting_str).child("Rating One").setValue(rating_one_check);
                mDatabase.child("Resources").child(posting_str).child("Rating Two").setValue(rating_two_check);
                mDatabase.child("Resources").child(posting_str).child("Rating Three").setValue(rating_three_check);
                mDatabase.child("Resources").child(posting_str).child("Rating Four").setValue(rating_four_check);
                mDatabase.child("Resources").child(posting_str).child("Rating Five").setValue(rating_five_check);
                mDatabase.child("Resources").child(posting_str).child("Geo Location").setValue(location_str);

                for (int i = 0; i < items_lst.size(); i += 1) {
                    String item_i_name = items_lst.get(i).getText().toString();
                    String item_i_quantity = quantities_lst.get(i).getText().toString();
                    mDatabase.child("Resources").child(posting_str).child("Items").child("Item " + (i + 1) + " Name").setValue(item_i_name);
                    mDatabase.child("Resources").child(posting_str).child("Items").child("Item " + (i + 1) + " Quantity").setValue(item_i_quantity);
                }

                Toast.makeText(PostResources.this, "Thanks for posting your resources!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PostResources.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Bitmap theImage = (Bitmap) data.getExtras().get("data");
            ImageView image = (ImageView) findViewById(R.id.imageView_PhotoSlot);
            image.setImageBitmap(theImage);
            str_photo = BitMapToString(theImage);

        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void configureButton() {
        Log.d("My location ", "Hi");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 1000, 1, locationListener);
    }
}