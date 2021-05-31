package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.DetailOrderHolder> {
    private Activity activity;
    private List<Product> productList;

    public DetailOrderAdapter(Activity activity)
    {
        this.activity = activity;
    }

    public void setData(List<Product> products)
    {
        productList = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_order_detail, parent, false);
        return new DetailOrderHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DetailOrderHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        if (productList == null)
            return 0;
        return productList.size();
    }

    //////////////////////////////////////////////////////////////////
    public class DetailOrderHolder extends RecyclerView.ViewHolder
    {
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
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().trim().equals("") || s.toString().trim().isEmpty())
                        productList.get(getAdapterPosition()).setAmount(0);
                    else productList.get(getAdapterPosition()).setAmount(Integer.parseInt(s.toString().trim()));
                    textViewSumPrice.setText((singleProduct.getPrice() * productList.get(getAdapterPosition()).getAmount()) + "");
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        @SuppressLint("SetTextI18n")
        public void bind(Product product)
        {
            singleProduct = product;
            textViewID.setText(singleProduct.getID());
            textViewName.setText(singleProduct.getName());
            textViewUnit.setText(singleProduct.getUnit());
            textViewPrice.setText(Integer.toString(singleProduct.getPrice()));
        }
    }
}
