package com.khgame.picturepuzzle2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkang on 2017/1/7.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new ClassicListFragment());
        fragments.add(new SerialListFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
