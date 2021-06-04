package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.DetailOrderActivity;
import com.example.myapplication.Order;
import com.example.myapplication.Product;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> implements Filterable
{
    private Activity activity;
    private List<Order> orderList;
    private List<Order> orderListFake;

    public OrderAdapter(Activity activity)
    {
        this.activity = activity;
    }

    public void setData(List<Order> orders)
    {
        orderList = orders;
        orderListFake = orderList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_order, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderAdapter.OrderHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        if(orderList == null)
            return 0;
        return orderList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty())
                    orderList = orderListFake;
                else {
                    List<Order> list = new ArrayList<>();
                    for (Order order : orderListFake) {
                        if (order.getUuid().toString().toLowerCase().contains(strSearch.toLowerCase())
                                || order.getCustomer().toLowerCase().contains(strSearch.toLowerCase()))
                            list.add(order);
                    }
                    orderList = list;
                }
                FilterResults results = new FilterResults();
                results.values = orderList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                orderList = (List<Order>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView orderId, orderCustomer, orderDate;
        private Order singleOrder;

        public OrderHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            orderId = (TextView) itemView.findViewById(R.id.order_id);
            orderDate = (TextView) itemView.findViewById(R.id.order_date);
            orderCustomer = (TextView) itemView.findViewById(R.id.order_customer_name);
        }

        public void bind(Order order1)
        {
            singleOrder = order1;
            orderId.setText(singleOrder.getUuid().toString());
            orderDate.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(singleOrder.getDateOrder()));
            orderCustomer.setText(singleOrder.getCustomer());
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(activity, DetailOrderActivity.class);
            i.putExtra("single_order", singleOrder.getUuid().toString());
            activity.startActivity(i);
        }
    }
}
