package com.khgame.picturepuzzle.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.di.ActivityScoped;
import com.khgame.picturepuzzle.ui.BaseFragment;
import com.khgame.picturepuzzle.ui.main.MainActivity;

import javax.inject.Inject;

@ActivityScoped
public class SplashFragment extends BaseFragment implements SplashContract.View {
    private static final int DURATION = 1000;

    ImageView logo;
    @Inject
    SplashContract.Presenter presenter;

    @Inject
    public SplashFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logo = view.findViewById(R.id.logo);
        presenter.takeView(this);
    }

    @Override
    public void showLogo() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(DURATION);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logo.startAnimation(alphaAnimation);
    }

    @Override
    public void showMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }
}
