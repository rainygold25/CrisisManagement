package com.example.crisismanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SearchResources extends AppCompatActivity {
    RecyclerView recView_result;
    EditText editText_search_text;
    Button button_search;
    DatabaseReference mDatabase;
    List<resourceItem> resourceItemList;
    resourceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_resources);

        resourceItemList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        recView_result = (RecyclerView) findViewById(R.id.recyclerView_items);
        recView_result.setHasFixedSize(true);
        recView_result.setLayoutManager(new LinearLayoutManager(this));



        editText_search_text = (EditText) findViewById(R.id.editText_searchResource);
        button_search = (Button) findViewById(R.id.button_search);

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
                                    while (!found_item && items_iterator.hasNext()) {
                                        DataSnapshot itemSnapshot = items_iterator.next();
                                        if (itemSnapshot.getValue().toString().toLowerCase().contains(search_text.toLowerCase())) {
                                            found_item = true;
                                        } else if (search_text.toLowerCase().contains(itemSnapshot.getValue().toString().toLowerCase())) {
                                            found_item = true;
                                        }
                                    }
                                    if (!found_item) {
                                        matches_search_text = false;
                                    }
                                }
                            }
                            if (matches_search_text) {
                                if (geo_location.length() > 0) {
                                    String[] lat_long_str = new String[2];
                                    lat_long_str = geo_location.split(" ");
                                    //TODO: calculate distance
                                }
                                resourceItem curr_item = new resourceItem(name, address, contact_info, "", image, "");
                                resourceItemList.add(curr_item);
                            }
                        }
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
}