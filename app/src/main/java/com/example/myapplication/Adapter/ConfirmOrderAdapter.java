package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Product;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderAdapter.ConfirmOrderHolder> {
    private Activity activity;
    private List<Product> productList;

    public ConfirmOrderAdapter(Activity activity)
    {
        this.activity = activity;
    }

    public void setData(List<Product> products)
    {
        productList = products;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ConfirmOrderHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_order_confirm, parent, false);
        return new ConfirmOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ConfirmOrderAdapter.ConfirmOrderHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        if (productList == null)
            return 0;
        return productList.size();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public class ConfirmOrderHolder extends RecyclerView.ViewHolder
    {
        private TextView textViewID, textViewName, textViewUnit, textViewPrice, textViewAmount, textViewSumPrice;
        private Product singleProduct;

        public ConfirmOrderHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewID = (TextView) itemView.findViewById(R.id.product_confirm_id);
            textViewName = (TextView) itemView.findViewById(R.id.product_confirm_name);
            textViewUnit = (TextView) itemView.findViewById(R.id.product_confirm_unit);
            textViewPrice = (TextView) itemView.findViewById(R.id.product_confirm_price);
            textViewAmount = (TextView) itemView.findViewById(R.id.product_confirm_sum_amount);
            textViewSumPrice = (TextView) itemView.findViewById(R.id.product_confirm_sum_price);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Product product)
        {
            singleProduct = product;
            textViewID.setText(singleProduct.getID());
            textViewName.setText(singleProduct.getName());
            textViewUnit.setText(singleProduct.getUnit());
            textViewPrice.setText(singleProduct.getPrice() + "");
            textViewAmount.setText(singleProduct.getAmount() + "");
            textViewSumPrice.setText((singleProduct.getPrice() * singleProduct.getAmount()) + "");
        }
    }
}
