package com.khgame.picturepuzzle.ui.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.di.ActivityScoped;
import com.khgame.picturepuzzle.ui.BaseFragment;

import javax.inject.Inject;

@ActivityScoped
public class MainFragment extends BaseFragment implements MainContract.View {

    ViewPager viewPager;
    FragmentPagerAdapter adapter;

    @Inject
    MainContract.Presenter presenter;

    @Inject
    public MainFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.view_pager);
        adapter = new MainFragmentAdapter(getChildFragmentManager());
        presenter.takeView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.dropView();
    }

    private static class MainFragmentAdapter extends FragmentPagerAdapter {

        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = null;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
