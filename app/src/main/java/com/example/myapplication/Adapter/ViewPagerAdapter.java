package com.example.myapplication.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.Fragment.FragmentOrderConfirm;
import com.example.myapplication.Fragment.FragmentOrderDetails;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new FragmentOrderDetails();
            case 1:
                return new FragmentOrderConfirm();
            default:
                return new FragmentOrderDetails();
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch (position)
        {
            case 0:
                title = "CHI TIẾT";
                break;
            case 1:
                title = "XÁC NHẬN";
                break;
        }
        return title;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
