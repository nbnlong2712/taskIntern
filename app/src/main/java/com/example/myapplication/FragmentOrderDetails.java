package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.DbProductHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentOrderDetails extends Fragment {
    private Button btnBack;
    private Button btnConfirm;
    private SearchView searchView;
    private TextView orderId, orderSKUs, orderSumAmount, orderSumPrice;
    private RecyclerView recyclerView;
    private Order order;
    private DetailOrderAdapter detailOrderAdapter;
    private List<Product> productList;

    @SuppressLint("SetTextI18n")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        DbProductHelper db = new DbProductHelper(getActivity());

        order = new Order();

        detailOrderAdapter = new DetailOrderAdapter(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.rcv_detail_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (db.getAllProductFromDB() == null) {
            productList = new ArrayList<>();
        } else {
            productList = db.getAllProductFromDB();
        }

        detailOrderAdapter.setData(productList);
        recyclerView.setAdapter(detailOrderAdapter);

        searchView = (SearchView) view.findViewById(R.id.search_view);
        orderId = (TextView) view.findViewById(R.id.order_detail_id);
        orderSKUs = (TextView) view.findViewById(R.id.order_detail_sku);
        orderSumAmount = (TextView) view.findViewById(R.id.order_detail_amount);
        orderSumPrice = (TextView) view.findViewById(R.id.order_detail_sum_price);

        btnConfirm = (Button) view.findViewById(R.id.confirm_btn_detail);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "HEHEHE", Toast.LENGTH_SHORT).show();
            }
        });

        orderId.setText(order.getUuid().toString());
        orderSKUs.setText(getOrderSKUs(productList) + "");
        orderSumAmount.setText(getOrderSumAmount(productList) + "");
        orderSumPrice.setText(getOrderSumPrice(productList) + "");

        btnBack = (Button) view.findViewById(R.id.back_btn_detail);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    public int getOrderSKUs(List<Product> productLists)
    {
        int s = 0;
        for (Product product : productLists)
        {
            if(product.getAmount() > 0)
                s++;
        }
        return s;
    }

    public int getOrderSumAmount(List<Product> productLists)
    {
        int s = 0;
        for (Product product : productLists)
            s += product.getAmount();
        return s;
    }

    public int getOrderSumPrice(List<Product> productLists)
    {
        int s = 0;
        for (Product product : productLists)
            s += product.getAmount() * product.getPrice();
        return s;
    }

}
