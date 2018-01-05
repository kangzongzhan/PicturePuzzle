package com.khgame.picturepuzzle.ui.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.di.ActivityScoped;
import com.khgame.picturepuzzle.ui.base.BaseDaggerFragment;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class MainFragment extends BaseDaggerFragment implements MainContract.View {

    ViewPager viewPager;

    @Inject
    Lazy<MainViewPagerAdapter> mainViewPagerAdapterLazy;

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
        viewPager.setAdapter(mainViewPagerAdapterLazy.get());
        presenter.takeView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.dropView();
    }

}
