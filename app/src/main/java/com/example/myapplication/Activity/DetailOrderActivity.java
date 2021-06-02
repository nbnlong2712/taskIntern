package com.example.myapplication.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ConfirmOrderAdapter;
import com.example.myapplication.Database.DbOrderHelper;
import com.example.myapplication.Database.DbOrderProductHelper;
import com.example.myapplication.Database.DbProductHelper;
import com.example.myapplication.Order;
import com.example.myapplication.OrderProduct;
import com.example.myapplication.Product;
import com.example.myapplication.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailOrderActivity extends AppCompatActivity {
    private Button btnBack;
    private Order order;
    private TextView tvOrderId, tvOrderDate, tvOrderCustomer, tvOrderPhone, tvOrderAddress, tvOrderNote, tvOrderSKUs, tvOrderAmount, tvOrderPrice;
    private ConfirmOrderAdapter detailOrderAdapter;
    private DbOrderHelper dbOrderHelper;
    private DbProductHelper dbProductHelper;
    private DbOrderProductHelper dbOrderProductHelper;
    private RecyclerView recyclerView;
    private List<Product> productList;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        dbOrderHelper = new DbOrderHelper(DetailOrderActivity.this);
        dbOrderHelper.getWritableDatabase();
        dbProductHelper = new DbProductHelper(DetailOrderActivity.this);
        dbProductHelper.getWritableDatabase();
        dbOrderProductHelper = new DbOrderProductHelper(DetailOrderActivity.this);
        dbOrderProductHelper.getWritableDatabase();

        Intent i = getIntent();
        order = dbOrderHelper.getOrderFromDB(i.getStringExtra("single_order"));

        if(getAllProductOrder(order.getUuid().toString()).size() == 0)
            productList = new ArrayList<>();
        else productList = getAllProductOrder(order.getUuid().toString());

        btnBack = (Button) findViewById(R.id.back_btn);
        tvOrderId = (TextView) findViewById(R.id.detail_order_id);
        tvOrderDate = (TextView) findViewById(R.id.detail_order_date);
        tvOrderCustomer = (TextView) findViewById(R.id.detail_order_customer);
        tvOrderPhone = (TextView) findViewById(R.id.detail_order_phone);
        tvOrderAddress = (TextView) findViewById(R.id.detail_order_address);
        tvOrderNote = (TextView) findViewById(R.id.detail_order_note);
        tvOrderSKUs = (TextView) findViewById(R.id.detail_order_sku);
        tvOrderAmount = (TextView) findViewById(R.id.detail_order_amount);
        tvOrderPrice = (TextView) findViewById(R.id.detail_order_sum_price);
        recyclerView = (RecyclerView) findViewById(R.id.rcv_order_detail);

        tvOrderId.setText(order.getUuid().toString());
        String datetime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(order.getDateOrder());
        tvOrderDate.setText(datetime);
        tvOrderCustomer.setText(order.getCustomer());
        tvOrderPhone.setText(order.getPhoneNumber());
        tvOrderAddress.setText(order.getAddress());
        tvOrderNote.setText(order.getNote());
        tvOrderAmount.setText(getOrderSumAmount(productList) + "");
        tvOrderSKUs.setText(productList.size() + "");
        tvOrderPrice.setText(getOrderSumPrice(productList) + "");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailOrderAdapter = new ConfirmOrderAdapter(this);
        detailOrderAdapter.setData(productList);
        recyclerView.setAdapter(detailOrderAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailOrderActivity.this, OrderActivity.class);
                startActivity(i);
            }
        });
    }

    public List<Product> getAllProductOrder(String id_order) {
        List<OrderProduct> orderProducts = dbOrderProductHelper.getAllOrderProductFromDB(id_order);
        List<Product> products = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            Product product = dbProductHelper.getProductFromDB(orderProduct.getId_product());
            product.setAmount(orderProduct.getAmount());
            products.add(product);
        }
        return products;
    }

    public int getOrderSumAmount(List<Product> productLists) {
        int s = 0;
        for (Product product : productLists)
            s += product.getAmount();
        return s;
    }

    public int getOrderSumPrice(List<Product> productLists) {
        int s = 0;
        for (Product product : productLists)
            s += product.getAmount() * product.getPrice();
        return s;
    }
}
