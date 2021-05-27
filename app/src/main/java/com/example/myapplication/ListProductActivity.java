package com.example.myapplication;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class ListProductActivity extends AppCompatActivity {
    private Spinner spnUnitProduct;
    private EditText inputProductID, inputProductName, inputProductPrice;
    private Button confirmBtn, addProductBtn;
    private ListProductAdapter listProductAdapter;
    private RecyclerView recyclerListProduct;
    private List<Product> productList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        Product product = new Product("", "", "", 0, 0);
        productList = getAllProductFromDb();

        listProductAdapter = new ListProductAdapter(getParent());
        recyclerListProduct = (RecyclerView) findViewById(R.id.rcv_list_product);
        recyclerListProduct.setLayoutManager(new LinearLayoutManager(this));

        spnUnitProduct = (Spinner) findViewById(R.id.unit_product);
        List<String> unitlist = new ArrayList<>();
        unitlist.add("Hộp");
        unitlist.add("Viên");
        unitlist.add("Chai");
        unitlist.add("Lốc");
        unitlist.add("Thùng");
        unitlist.add("Bịch");
        unitlist.add("Gói");
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,unitlist);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnUnitProduct.setAdapter(adapter);


        spnUnitProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product.setUnit(spnUnitProduct.getSelectedItem().toString());
                Log.i("TAG", product.getUnit());
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
                product.setID(s.toString());
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
                product.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputProductPrice = (EditText) findViewById(R.id.price_product);


        confirmBtn = (Button) findViewById(R.id.confirm_btn);

        addProductBtn = (Button) findViewById(R.id.add_product_btn);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strID = inputProductID.getText().toString();
                String strName = inputProductName.getText().toString();
                String strPrice = inputProductPrice.getText().toString();
                if(strID==null||strID.isEmpty()||strName==null||strName.isEmpty()||strPrice==null||strPrice.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please điền đầy đủ!", Toast.LENGTH_SHORT).show();
                }
                else if (!isNumeric(strPrice))
                {
                    Toast.makeText(getApplicationContext(), "Điền lại giá tiền!", Toast.LENGTH_SHORT).show();
                }
                else {
                    product.setPrice(Integer.parseInt(strPrice));
                    productList.add(product);
                    Toast.makeText(getApplicationContext(), "Đã thêm sản phẩm " + product.getName(), Toast.LENGTH_SHORT).show();
                    listProductAdapter.setData(productList);
                    listProductAdapter.notifyDataSetChanged();
                    recyclerListProduct.setAdapter(listProductAdapter);
                    inputProductID.setText("");
                    inputProductName.setText("");
                    inputProductPrice.setText("0");
                    spnUnitProduct.setSelection(adapter.getPosition("Hộp"));

                    Log.i("TAG",  "so luong " + productList.size() + "");
                }
            }
        });
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public List<Product> getAllProductFromDb()
    {
        productList = new ArrayList<>();
        SQLiteDatabase db = openOrCreateDatabase("Product.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from Product", null);
        while (cursor.moveToNext())
        {
            String productId = cursor.getString(0);
            String productName = cursor.getString(1);
            String productUnit = cursor.getString(2);
            int productPrice = cursor.getInt(3);
            Product prod = new Product(productId, productName, productUnit,0, productPrice);
            productList.add(prod);
        }
        if(listProductAdapter == null) {
            listProductAdapter = new ListProductAdapter(getParent());
            listProductAdapter.setData(productList);
            recyclerListProduct.setAdapter(listProductAdapter);
        }
        else {
            listProductAdapter.setData(productList);
            recyclerListProduct.setAdapter(listProductAdapter);
        }
        return productList;
    }

}
