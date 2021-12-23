package com.example.crisismanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button postResources = (Button) findViewById(R.id.button_post_resources);
        Button searchResources = (Button) findViewById(R.id.button_search_resources);
        Button sendlocation = (Button) findViewById(R.id.button_send_location);

        postResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostResources.class);
                startActivity(intent);
            }
        });
        searchResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchResources.class);
                startActivity(intent);
            }
        });
        sendlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SendLocation.class);
                startActivity(intent);
            }
        });
    }
}