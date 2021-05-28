package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.DbProductHelper;

import java.util.ArrayList;
import java.util.List;

public class ListProductActivity extends AppCompatActivity {
    private Spinner spnUnitProduct;
    private EditText inputProductID, inputProductName, inputProductPrice;
    private Button confirmBtn, addProductBtn, backBtn;
    private ListProductAdapter listProductAdapter;
    private RecyclerView recyclerListProduct;
    private List<Product> productList;
    private DbProductHelper dbProductHelper;
    private String prodId = "", prodName = "", prodUnit = "", prodPrice = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        dbProductHelper = new DbProductHelper(ListProductActivity.this);
        dbProductHelper.getWritableDatabase();

        listProductAdapter = new ListProductAdapter(this);
        recyclerListProduct = (RecyclerView) findViewById(R.id.rcv_list_product);
        recyclerListProduct.setLayoutManager(new LinearLayoutManager(this));

        if (getAllProductFromDb() == null) {
            productList = new ArrayList<>();
        } else {
            productList = getAllProductFromDb();
        }

        spnUnitProduct = (Spinner) findViewById(R.id.unit_product);
        List<String> unitlist = new ArrayList<>();
        unitlist.add("Hộp");
        unitlist.add("Viên");
        unitlist.add("Chai");
        unitlist.add("Lốc");
        unitlist.add("Thùng");
        unitlist.add("Bịch");
        unitlist.add("Gói");
        unitlist.add("Cái");
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, unitlist);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnUnitProduct.setAdapter(adapter);

        spnUnitProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prodUnit = spnUnitProduct.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        inputProductID = (EditText) findViewById(R.id.id_product);
        inputProductID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prodId = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputProductName = (EditText) findViewById(R.id.name_product);
        inputProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prodName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputProductPrice = (EditText) findViewById(R.id.price_product);
        inputProductPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prodPrice = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        backBtn = (Button) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListProductActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        addProductBtn = (Button) findViewById(R.id.add_product_btn);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strID = inputProductID.getText().toString();
                String strName = inputProductName.getText().toString();
                String strPrice = inputProductPrice.getText().toString();
                Product product = new Product(strID, strName, prodUnit, 0);
                if (strID == null || strID.isEmpty() || strName == null || strName.isEmpty() || strPrice == null || strPrice.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please điền đầy đủ!", Toast.LENGTH_SHORT).show();
                } else if (!isNumeric(strPrice)) {
                    Toast.makeText(getApplicationContext(), "Điền lại giá tiền!", Toast.LENGTH_SHORT).show();
                } else if (!isExistInProductList(productList, product))
                    Toast.makeText(getApplicationContext(), "Mã hoặc tên sản phẩm đã tồn tại!", Toast.LENGTH_SHORT).show();
                else {
                    product.setPrice(Integer.parseInt(strPrice));
                    productList.add(new Product(prodId, prodName, prodUnit, Integer.parseInt(prodPrice)));

                    Toast.makeText(getApplicationContext(), "Đã thêm sản phẩm " + product.getName(), Toast.LENGTH_SHORT).show();

                    listProductAdapter.setData(productList);
                    listProductAdapter.notifyDataSetChanged();
                    recyclerListProduct.setAdapter(listProductAdapter);

                    inputProductID.setText("");
                    inputProductName.setText("");
                    inputProductPrice.setText("0");
                    spnUnitProduct.setSelection(adapter.getPosition("Hộp"));
                    Log.i("TAiGA", productList.size()+"");

                }
            }
        });

        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Đã lưu!", Toast.LENGTH_SHORT).show();
                confirmProduct(productList);
            }
        });
    }

    public boolean isExistInProductList(List<Product> productList, Product product) {
        int linh = 0;
        for (int i = 0; i < productList.size(); i++) {
            if (product.getID().equals(productList.get(i).getID()) || product.getName().equals(productList.get(i).getName()))
                linh = 1;
        }
        if (linh == 1)
            return false;
        else return true;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<Product> getAllProductFromDb() {
        productList = new ArrayList<>();
        SQLiteDatabase db = openOrCreateDatabase("Product.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from Product", null);
        while (cursor.moveToNext()) {
            String productId = cursor.getString(0);
            String productName = cursor.getString(1);
            String productUnit = cursor.getString(2);
            int productPrice = cursor.getInt(3);
            Product prod = new Product(productId, productName, productUnit, productPrice);
            productList.add(prod);
        }
        if (listProductAdapter == null) {
            listProductAdapter = new ListProductAdapter(getParent());
            listProductAdapter.setData(productList);
            recyclerListProduct.setAdapter(listProductAdapter);
        } else {
            listProductAdapter.setData(productList);
            recyclerListProduct.setAdapter(listProductAdapter);
        }
        return productList;
    }

    public void confirmProduct(List<Product> productList) {
        if(getAllProductFromDb().size() == 0)
        {
            for (Product product : productList) {
                dbProductHelper.insertData(product.getID(), product.getName(), product.getUnit(), product.getPrice());
            }
        }
        else
        {
            for(Product product : productList)
            {
                if(!dbProductHelper.checkID(product.getID()) && !dbProductHelper.checkName(product.getName()))
                    dbProductHelper.insertData(product.getID(), product.getName(), product.getUnit(), product.getPrice());
                getAllProductFromDb();
            }
        }
    }
}