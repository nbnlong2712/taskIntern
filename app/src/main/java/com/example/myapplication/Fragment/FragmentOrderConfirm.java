package com.example.myapplication.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.CreateOrderActivity;
import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.Adapter.ConfirmOrderAdapter;
import com.example.myapplication.Database.DbOrderHelper;
import com.example.myapplication.Database.DbOrderProductHelper;
import com.example.myapplication.Database.DbProductHelper;
import com.example.myapplication.OrderProduct;
import com.example.myapplication.Product;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FragmentOrderConfirm extends Fragment {
    private Button btnBack, btnConfirm;
    private String id_Order = "";
    private TextView textViewID, textViewDate, textViewSKUs, textViewSumAmount, textViewSumPrice;
    private CreateOrderActivity createOrderActivity;
    private DbOrderHelper dbOrderHelper;
    private DbProductHelper dbProductHelper;
    private DbOrderProductHelper dbOrderProductHelper;
    private RecyclerView recyclerView;
    private List<String> orderInfo = new ArrayList<>();
    private ConfirmOrderAdapter confirmOrderAdapter;
    private List<Product> productList;
    private EditText edtCustomer, edtPhoneNumber, edtAddress, edtNote;
    private String strCustomer = "", strPhoneNumber = "", strAddress = "", strNote = "";

    public FragmentOrderConfirm() {
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_confirm, container, false);

        // Mở database
        dbOrderHelper = new DbOrderHelper(getActivity());
        dbOrderHelper.getWritableDatabase();
        dbProductHelper = new DbProductHelper(getActivity());
        dbProductHelper.getWritableDatabase();
        dbOrderProductHelper = new DbOrderProductHelper(getActivity());
        dbOrderProductHelper.getWritableDatabase();

        // Lấy ID của đơn hàng thông qua hàm get của activity
        createOrderActivity = (CreateOrderActivity) getActivity();
        id_Order = createOrderActivity.getOrderId();
        orderInfo = dbOrderHelper.getOrderInfoFromDB(id_Order);

        // Ánh xạ view
        textViewID = (TextView) view.findViewById(R.id.order_confirm_id);
        textViewDate = (TextView) view.findViewById(R.id.order_confirm_date);
        textViewSKUs = (TextView) view.findViewById(R.id.order_confirm_sku);
        textViewSumAmount = (TextView) view.findViewById(R.id.order_confirm_amount);
        textViewSumPrice = (TextView) view.findViewById(R.id.order_confirm_sum_price);
        edtCustomer = (EditText) view.findViewById(R.id.order_customer);
        edtPhoneNumber = (EditText) view.findViewById(R.id.order_phone_number);
        edtAddress = (EditText) view.findViewById(R.id.order_address);
        edtNote = (EditText) view.findViewById(R.id.order_note);

        recyclerView = (RecyclerView) view.findViewById(R.id.rcv_confirm_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        confirmOrderAdapter = new ConfirmOrderAdapter(getActivity());

        if (getAllProductOrder(id_Order).size() == 0)
            productList = new ArrayList<>();
        else {
            productList = getAllProductOrder(id_Order);
            textViewSKUs.setText(productList.size() + "");
            textViewSumAmount.setText(getOrderSumAmount(productList) + "");
            textViewSumPrice.setText(getOrderSumPrice(productList) + "");
        }
        confirmOrderAdapter.setData(productList);
        recyclerView.setAdapter(confirmOrderAdapter);

        btnBack = (Button) view.findViewById(R.id.back_btn_confirm);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbOrderHelper.deleteOrder(id_Order);
                dbOrderProductHelper.deleteAllFromDB(id_Order);
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        edtCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strCustomer = s.toString().trim();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strPhoneNumber = s.toString().trim();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strAddress = s.toString().trim();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strNote = s.toString().trim();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnConfirm = (Button) view.findViewById(R.id.confirm_btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strCustomer.equals("") || strCustomer.isEmpty() || strPhoneNumber.equals("") || strPhoneNumber.isEmpty() ||
                        strAddress.equals("") || strAddress.isEmpty() || strNote.equals("") || strNote.isEmpty()) {
                    Toast.makeText(getActivity(), "Điền đầy đủ thông tin trước khi đặt hàng!" , Toast.LENGTH_SHORT).show();
                }
                else {
                    dbOrderHelper.updateCustomerInfo(id_Order, strCustomer, strPhoneNumber, strAddress, strNote);
                    Toast.makeText(getActivity(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                }
            }
        });

        textViewID.setText(id_Order);
        if (orderInfo.size() != 0) {
            textViewDate.setText(orderInfo.get(0));
        }
        return view;
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
