package com.khgame.picturepuzzle.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.khgame.picturepuzzle.di.ActivityScoped;
import com.khgame.picturepuzzle.ui.main.classic.ClassicFragment;
import com.khgame.picturepuzzle.ui.main.serial.SerialFragment;

import javax.inject.Inject;

@ActivityScoped
public class MainViewPagerAdapter extends FragmentPagerAdapter  {

    @Inject
    public MainViewPagerAdapter(MainFragment mainFragment) {
        super(mainFragment.getChildFragmentManager());
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ClassicFragment();
        } else {
            return new SerialFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
