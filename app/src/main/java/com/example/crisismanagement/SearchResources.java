package com.example.crisismanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SearchResources extends AppCompatActivity {
    public static RecyclerView recView_result;
    EditText editText_search_text;
    Button button_search;
    DatabaseReference mDatabase;
    List<resourceItem> resourceItemList;
    resourceAdapter adapter;
    String API_KEY = "AIzaSyBcRVo6fkmdbqDO8chiYo3MK1Css1IseSU";
    private LocationManager locationManager;
    private LocationListener locationListener;
    MultiSelectionSpinner spinner_select_categories;
    Spinner spinner_select_distance;
    String curr_loc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_resources);

        spinner_select_categories = (MultiSelectionSpinner) findViewById(R.id.spinner_select_categories);
        spinner_select_distance = (Spinner) findViewById(R.id.spinner_select_distance);

        spinner_select_categories.setItems(new String[]{"Any", "Food", "Clothing", "Personal", "Safety", "Communication", "Comfort", "Pet"});
        spinner_select_categories.setSelection(new String[]{"Any"});

        ArrayAdapter<String> distances_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Any", "1 mile", "3 miles", "5 miles"});
        spinner_select_distance.setAdapter(distances_adapter);

        resourceItemList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        recView_result = (RecyclerView) findViewById(R.id.recyclerView_items);
        recView_result.setHasFixedSize(true);
        recView_result.setLayoutManager(new LinearLayoutManager(this));

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        editText_search_text = (EditText) findViewById(R.id.editText_searchResource);
        button_search = (Button) findViewById(R.id.button_search);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                curr_loc = location.getLatitude() + "," + location.getLongitude();
                Log.d("My Location", curr_loc);
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
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            } else {
                locationManager.requestLocationUpdates("gps", 1000, 1, locationListener);
            }
        } else {
            locationManager.requestLocationUpdates("gps", 1000, 1, locationListener);
        }

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_text = editText_search_text.getText().toString();
                mDatabase.child("Resources").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        resourceItemList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String name = "";
                            String address = "";
                            String image = "";
                            String date = "";
                            String contact_info = "";
                            String geo_location = "";
                            String distance = "";
                            List<String> items = new ArrayList<>();
                            List<String> quantities = new ArrayList<>();
                            List<String> categories_present = new ArrayList<>();
                            HashSet<String> all_categories = new HashSet<>();
                            all_categories.add("Food");
                            all_categories.add("Clothing");
                            all_categories.add("Personal");
                            all_categories.add("Safety");
                            all_categories.add("Communication");
                            all_categories.add("Pet");
                            all_categories.add("Comfort");
                            String rating = "";
                            List<String> category_selections = spinner_select_categories.getSelectedStrings();
                            String distance_selection = spinner_select_distance.getSelectedItem().toString();
                            boolean found_category = false;
                            if (category_selections.contains("Any")) {
                                found_category = true;
                            }
                            Iterator<DataSnapshot> postings_iterator = dataSnapshot.getChildren().iterator();
                            boolean matches_search_text = true;
                            while (postings_iterator.hasNext() && matches_search_text) {
                                DataSnapshot bodySnapshot = postings_iterator.next();

                                if (bodySnapshot.getKey().equals("Name")) {
                                    name = bodySnapshot.getValue().toString();
                                } else if (bodySnapshot.getKey().equals("Address")) {
                                    address = bodySnapshot.getValue().toString();
                                } else if (bodySnapshot.getKey().equals("Image")) {
                                    image = bodySnapshot.getValue().toString();
                                } else if (bodySnapshot.getKey().equals("Contact Info")) {
                                    contact_info = bodySnapshot.getValue().toString();
                                } else if (bodySnapshot.getKey().equals("Geo Location")) {
                                    geo_location = bodySnapshot.getValue().toString();
                                } else if (bodySnapshot.getKey().equals("Items")) {
                                    Iterator<DataSnapshot> items_iterator = bodySnapshot.getChildren().iterator();
                                    boolean found_item = false;
                                    int count = 0;
                                    while (items_iterator.hasNext()) {
                                        DataSnapshot itemSnapshot = items_iterator.next();
                                        String curr_value = itemSnapshot.getValue().toString();
                                        if (count % 2 == 0) {
                                            items.add(curr_value);
                                        } else {
                                            quantities.add(curr_value);
                                        }
                                        if (itemSnapshot.getValue().toString().toLowerCase().contains(search_text.toLowerCase())) {
                                            found_item = true;
                                        } else if (search_text.toLowerCase().contains(itemSnapshot.getValue().toString().toLowerCase())) {
                                            found_item = true;
                                        }
                                        count += 1;
                                    }
                                    if (!found_item) {
                                        matches_search_text = false;
                                    }
                                } else if (all_categories.contains(bodySnapshot.getKey())) {
                                    Log.d("category", bodySnapshot.getKey());
                                    if (bodySnapshot.getValue().equals("true")) {
                                        categories_present.add(bodySnapshot.getKey());
                                        if (category_selections.contains(bodySnapshot.getKey())) {
                                            found_category = true;
                                        }
                                    }

                                } else if (bodySnapshot.getKey().contains("Rating")) {
                                    if (bodySnapshot.getValue().equals("true")) {
                                        rating = bodySnapshot.getKey().split(" ")[1];
                                    }
                                }
                            }
                            if (matches_search_text && found_category) {
                                Log.d("geo_location", geo_location);
                                boolean within_radius = false;
                                if (distance_selection.equals("Any")) {
                                    within_radius = true;
                                }
                                if (geo_location.length() > 0) {
                                    HashMap<String, Integer> str_to_miles = new HashMap<>();
                                    str_to_miles.put("1 mile", 1);
                                    str_to_miles.put("3 miles", 3);
                                    str_to_miles.put("5 miles", 5);

                                    String[] lat_long_str = new String[2];
                                    lat_long_str = geo_location.split(" ");
                                    //TODO: calculate distance
                                    Log.d("curr_loc", curr_loc);
                                    while (curr_loc.length() == 0) {
                                        //Do nothing.
                                    }
                                    String[] curr_lat_long_str = new String[2];
                                    curr_lat_long_str = curr_loc.split(",");
                                    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + curr_lat_long_str[0] + "%2C" + curr_lat_long_str[1] + "&destinations=side_of_road%3A" + lat_long_str[0] + "%2C" + lat_long_str[1] + "&key=" + API_KEY;
                                    Log.d("httpurl", url);
                                    DownloadTaskAddress da = new DownloadTaskAddress();
                                    try {
                                        String result = da.execute(url).get();
                                        JSONObject jsonObject = new JSONObject(result);
                                        distance = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text");
                                        String distance_digits = distance.split(" ")[0];
                                        String distance_parsable_digits = "";
                                        for (int i = 0; i < distance_digits.length(); i += 1) {
                                            if (distance_digits.charAt(i) != ',') {
                                                distance_parsable_digits += distance_digits.charAt(i);
                                            }
                                        }
                                        double num_miles = Double.parseDouble(distance_parsable_digits);
                                        if (!distance_selection.equals("Any") && num_miles <= str_to_miles.get(distance_selection)) {
                                            within_radius = true;
                                        }
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    within_radius = true;
                                }
                                Log.d("Categories len list", categories_present.size() + "");
                                Log.d("Items len lst", items.size() + "");
                                resourceItem curr_item = new resourceItem(name, address, contact_info, "", image, distance, items, quantities, categories_present, rating, geo_location);
                                if (within_radius) {
                                    resourceItemList.add(curr_item);
                                }
                            }
                        }
                        Log.d("tail resourceItemList", resourceItemList.size() + "");
                        resourceItem[] resourceItems = new resourceItem[resourceItemList.size()];
                        resourceItems = resourceItemList.toArray(resourceItems);
                        adapter = new resourceAdapter(resourceItems, SearchResources.this);
                        recView_result.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                return;
        }
    }
}