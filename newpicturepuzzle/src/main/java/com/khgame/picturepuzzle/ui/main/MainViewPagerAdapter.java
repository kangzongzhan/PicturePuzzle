package com.khgame.picturepuzzle.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.khgame.picturepuzzle.di.FragmentScoped;
import com.khgame.picturepuzzle.ui.main.classic.ClassicContract;
import com.khgame.picturepuzzle.ui.main.classic.ClassicFragment;

import javax.inject.Inject;

@FragmentScoped
public class MainViewPagerAdapter extends FragmentPagerAdapter  {

    @Inject
    ClassicFragment classicFragment;

    @Inject
    public MainViewPagerAdapter(MainFragment mainFragment) {
        super(mainFragment.getChildFragmentManager());
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return classicFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
