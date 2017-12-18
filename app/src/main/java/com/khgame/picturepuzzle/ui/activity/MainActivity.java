package com.khgame.picturepuzzle.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.Toast;

import com.khgame.picturepuzzle.base.SquaredActivity;
import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.data.Serial;
import com.khgame.picturepuzzle.data.source.AppRepository;
import com.khgame.picturepuzzle.ui.fragment.ClassicListFragment;
import com.khgame.picturepuzzle.ui.fragment.SerialListFragment;
import com.khgame.picturepuzzle.ui.view.TabView;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends SquaredActivity {

    @BindView(R.id.main_viewpager)
    ViewPager viewPager;

    TabView tabView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initToolbar(toolbar);
        ButterKnife.bind(this);
        viewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(onPageChangeListener);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        setWindowStatusBarColor(getResources().getColor(R.color.colorPrimary));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppRepository appRepository = AppRepository.getINSTANCE();
        appRepository.getSerials()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(serials -> {
                    for (Serial serial: serials) {
                        Toast.makeText(MainActivity.this, serial.getId(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initToolbar(Toolbar toolbar) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tabView = new TabView(this);
        tabView.setOnTabClickListener(new TabView.OnTabSelectListener() {
            @Override
            public void onLeftSelect() {
                MainActivity.this.viewPager.setCurrentItem(0);
            }

            @Override
            public void onRightSelect() {
                MainActivity.this.viewPager.setCurrentItem(1);
            }
        });
        int tabWidth = getResources().getDimensionPixelSize(R.dimen.tab_width);
        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        toolbar.addView(tabView, new Toolbar.LayoutParams(tabWidth, tabHeight, Gravity.CENTER));
    }

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

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                tabView.selectLeft();
            } else {
                tabView.selectRight();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
