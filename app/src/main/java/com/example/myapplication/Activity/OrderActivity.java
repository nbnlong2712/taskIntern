package com.example.myapplication.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.OrderAdapter;
import com.example.myapplication.Database.DbOrderHelper;
import com.example.myapplication.Database.DbOrderProductHelper;
import com.example.myapplication.Order;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private Button btnBack;
    private TextView textView;
    private SearchView searchView;
    private List<Order> orderList;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private DbOrderHelper dbOrderHelper;
    private DbOrderProductHelper dbOrderProductHelper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_order);

        dbOrderHelper = new DbOrderHelper(OrderActivity.this);
        dbOrderHelper.getWritableDatabase();
        dbOrderProductHelper = new DbOrderProductHelper(OrderActivity.this);
        dbOrderProductHelper.getWritableDatabase();

        if(getValidOrder().size() == 0)
            orderList = new ArrayList<>();
        else orderList = getValidOrder();

        btnBack = (Button) findViewById(R.id.back_btn);
        textView = (TextView) findViewById(R.id.amount_orders);
        searchView = (SearchView) findViewById(R.id.search_order);
        recyclerView = (RecyclerView) findViewById(R.id.rcv_list_order);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(this);
        orderAdapter.setData(orderList);
        recyclerView.setAdapter(orderAdapter);

        textView.setText(orderList.size() + "");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public List<Order> getValidOrder()
    {
        List<Order> list1 = new ArrayList<>();
        List<Order> list = dbOrderHelper.getAllOrderFromDB();
        for (Order order : list)
        {
            if (order.getCustomer().equals("") || order.getCustomer().isEmpty())
            {
                dbOrderHelper.deleteOrder(order.getUuid().toString());
                dbOrderProductHelper.deleteAllFromDB(order.getUuid().toString());
            }
            else {
                list1.add(order);
            }
        }
        return list1;
    }
}
