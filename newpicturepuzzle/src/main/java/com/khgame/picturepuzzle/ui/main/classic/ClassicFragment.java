package com.khgame.picturepuzzle.ui.main.classic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khgame.picturepuzzle.di.ActivityScoped;
import com.khgame.picturepuzzle.ui.BaseFragment;

import javax.inject.Inject;

@ActivityScoped
public class ClassicFragment extends BaseFragment implements ClassicContract.View{
    @Inject
    ClassicContract.Presenter presenter;

    @Inject
    public ClassicFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(container.getContext());
        textView.setText("Classic Fragment");
        return textView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.takeView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.dropView();
    }
}
