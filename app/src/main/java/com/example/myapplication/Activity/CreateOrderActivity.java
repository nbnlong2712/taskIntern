package com.example.myapplication.Activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapter.ViewPagerAdapter;
import com.example.myapplication.Fragment.FragmentOrderConfirm;
import com.example.myapplication.Fragment.FragmentOrderDetails;
import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;

public class CreateOrderActivity extends AppCompatActivity implements FragmentOrderDetails.ISendIdListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String orderId = "";
    private FrameLayout frameLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_order);
        viewPager = (ViewPager) findViewById(R.id.view_pager_order);
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout_viewpager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void sendIdOrder(String idOrder) {
        orderId = idOrder;
        setOrderId(idOrder);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_order_confirm, new FragmentOrderConfirm());
        fragmentTransaction.commit();
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
