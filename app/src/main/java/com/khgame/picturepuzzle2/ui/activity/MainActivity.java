package com.khgame.picturepuzzle2.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.khgame.picturepuzzle.base.SquaredActivity;
import com.khgame.picturepuzzle2.R;
import com.khgame.picturepuzzle2.ui.fragment.ClassicListFragment;
import com.khgame.picturepuzzle2.ui.fragment.SerialListFragment;
import com.khgame.picturepuzzle2.ui.view.TabView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            if(position == 0) {
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
