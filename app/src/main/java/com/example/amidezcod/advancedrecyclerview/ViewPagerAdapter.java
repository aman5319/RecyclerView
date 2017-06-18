package com.example.amidezcod.advancedrecyclerview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amidezcod on 17/6/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public final List<Fragment> listOfFragments = new ArrayList<>();
    public final List<String> fragmentTitle = new ArrayList<>();
    public Context context;
    public ViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return listOfFragments.get(position);
    }

    @Override
    public int getCount() {
        return listOfFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
    public void addFragments(Fragment fragment , String s){
        listOfFragments.add(fragment);
        fragmentTitle.add(s);
    }
}
