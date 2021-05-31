package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.DbOrderHelper;
import com.example.myapplication.Database.DbOrderProductHelper;
import com.example.myapplication.Database.DbProductHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
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
    private DbProductHelper dbProductHelper;
    private DbOrderProductHelper dbOrderProductHelper;
    private DbOrderHelper dbOrderHelper;
    private List<Product> productOrderList;

    @SuppressLint("SetTextI18n")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        orderId = (TextView) view.findViewById(R.id.order_detail_id);
        orderSKUs = (TextView) view.findViewById(R.id.order_detail_sku);
        orderSumAmount = (TextView) view.findViewById(R.id.order_detail_amount);
        orderSumPrice = (TextView) view.findViewById(R.id.order_detail_sum_price);

        dbProductHelper = new DbProductHelper(getActivity());
        dbProductHelper.getWritableDatabase();
        dbOrderProductHelper = new DbOrderProductHelper(getActivity());
        dbOrderProductHelper.getWritableDatabase();
        dbOrderHelper = new DbOrderHelper(getActivity());
        dbOrderHelper.getWritableDatabase();

        order = new Order();

        if(dbProductHelper.getAllProductFromDB().size() == 0)
            productOrderList = new ArrayList<>();
        else productOrderList = dbProductHelper.getAllProductFromDB();
        detailOrderAdapter = new DetailOrderAdapter(productOrderList);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcv_detail_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(detailOrderAdapter);

        btnConfirm = (Button) view.findViewById(R.id.confirm_btn_detail);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOrderSumAmount(productOrderList) == 0)
                    Toast.makeText(getActivity(), "Hãy chọn mặt hàng muốn mua!", Toast.LENGTH_SHORT).show();
                else {
                    confirmOrder();
                    Toast.makeText(getActivity(), "Đã lưu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        orderId.setText(order.getUuid().toString());

        searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                detailOrderAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                detailOrderAdapter.getFilter().filter(newText);
                return false;
            }
        });

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

    public int getOrderSKUs(List<Product> productLists) {
        int s = 0;
        for (Product product : productLists) {
            if (product.getAmount() > 0)
                s++;
        }
        orderSKUs.setText(s + "");
        return s;
    }

    public int getOrderSumAmount(List<Product> productLists) {
        int s = 0;
        for (Product product : productLists)
            s += product.getAmount();
        orderSumAmount.setText(s + "");
        return s;
    }

    public int getOrderSumPrice(List<Product> productLists) {
        int s = 0;
        for (Product product : productLists)
            s += product.getAmount() * product.getPrice();
        orderSumPrice.setText(s + "");
        return s;
    }

    public void confirmOrder() {
        if (dbOrderHelper.checkOrderExists(order.getUuid().toString()) == null)
            dbOrderHelper.insertData(order.getUuid().toString()
                    , order.getDateOrder().getTime()
                    , "", "", "", "");

        dbOrderProductHelper.deleteAllFromDB(order.getUuid().toString());

        List<Product> products = dbProductHelper.getAllProductFromDB();
        for (Product product : products) {
            if (product.getAmount() != 0) {
                dbOrderProductHelper.insertData(order.getUuid().toString(), product.getID(), product.getAmount());
            }
        }
        Log.i("TAGGGGG", dbOrderProductHelper.getAllOrderProductFromDB(order.getUuid().toString()).size() + "");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.DetailOrderHolder> implements Filterable {
        private List<Product> productList;
        private List<Product> productListFake;

        public DetailOrderAdapter(List<Product> products) {
            this.productList = products;
            this.productListFake = productList;
        }

        @NonNull
        @Override
        public DetailOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_order_detail, parent, false);
            return new DetailOrderHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull DetailOrderAdapter.DetailOrderHolder holder, int position) {
            Product product = productList.get(position);
            holder.bind(product);
        }

        @Override
        public int getItemCount() {
            if (productList == null)
                return 0;
            return productList.size();
        }

        public void setProductList(List<Product> products) {
            productList = products;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String strSearch = constraint.toString();
                    if (strSearch.isEmpty())
                        productList = productListFake;
                    else {
                        List<Product> list = new ArrayList<>();
                        for (Product product : productListFake) {
                            if (product.getID().toLowerCase().contains(strSearch.toLowerCase())
                                    || product.getName().toLowerCase().contains(strSearch.toLowerCase()))
                                list.add(product);
                        }
                        productList = list;
                    }
                    FilterResults results = new FilterResults();
                    results.values = productList;

                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    productList = (List<Product>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////
        public class DetailOrderHolder extends RecyclerView.ViewHolder {
            private TextView textViewID, textViewName, textViewUnit, textViewPrice, textViewSumPrice;
            private EditText amountProduct;
            private Product singleProduct;

            public DetailOrderHolder(@NonNull View itemView) {
                super(itemView);
                textViewID = (TextView) itemView.findViewById(R.id.product_detail_id);
                textViewName = (TextView) itemView.findViewById(R.id.product_detail_name);
                textViewUnit = (TextView) itemView.findViewById(R.id.product_detail_unit);
                textViewPrice = (TextView) itemView.findViewById(R.id.product_detail_price);
                textViewSumPrice = (TextView) itemView.findViewById(R.id.product_detail_sum_price);

                amountProduct = (EditText) itemView.findViewById(R.id.product_detail_amount);
                amountProduct.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.toString().trim().equals("") || s.toString().trim().isEmpty()) {
                            productList.get(getAdapterPosition()).setAmount(0);
                            productOrderList.get(getAdapterPosition()).setAmount(0);
                        } else {
                            productList.get(getAdapterPosition()).setAmount(Integer.parseInt(s.toString().trim()));
                            productOrderList.get(getAdapterPosition()).setAmount(Integer.parseInt(s.toString().trim()));
                        }
                        textViewSumPrice.setText((singleProduct.getPrice() * productList.get(getAdapterPosition()).getAmount()) + "");
                        getOrderSKUs(productList);
                        getOrderSumAmount(productList);
                        getOrderSumPrice(productList);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }

            @SuppressLint("SetTextI18n")
            public void bind(Product product) {
                singleProduct = product;
                textViewID.setText(singleProduct.getID());
                textViewName.setText(singleProduct.getName());
                textViewUnit.setText(singleProduct.getUnit());
                textViewPrice.setText(Integer.toString(singleProduct.getPrice()));
                amountProduct.setText(singleProduct.getAmount() + "");
            }
        }
    }
}
