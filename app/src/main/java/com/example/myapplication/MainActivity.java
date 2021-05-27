package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Database.DbProductHelper;

public class MainActivity extends AppCompatActivity {
    private Button listProductBtn;
    private Button createOrderBtn;
    private Button exitBtn;
    private DbProductHelper dbProductHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbProductHelper = new DbProductHelper(MainActivity.this);
        dbProductHelper.getWritableDatabase();

        listProductBtn = (Button) findViewById(R.id.list_product_btn);
        createOrderBtn = (Button) findViewById(R.id.create_order_btn);
        exitBtn = (Button) findViewById(R.id.exit_btn);

        listProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListProductActivity.class);
                startActivity(i);
            }
        });

        createOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateOrderActivity.class);
                startActivity(i);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}