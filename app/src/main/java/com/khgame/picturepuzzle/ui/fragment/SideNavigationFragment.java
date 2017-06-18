package com.khgame.picturepuzzle.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.khgame.picturepuzzle.BuildConfig;
import com.khgame.picturepuzzle.R;
import com.khgame.sdk.picturepuzzle.base.SquaredFragment;
import com.khgame.sdk.picturepuzzle.common.Constant;
import com.khgame.sdk.picturepuzzle.common.SettingManager;
import com.khgame.sdk.picturepuzzle.events.ClassicDisorderPreviewSettingChange;
import com.khgame.sdk.picturepuzzle.events.SerialDisorderPreviewSettingChange;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zkang on 2017/6/3.
 */

public class SideNavigationFragment extends SquaredFragment {

    @BindView(R.id.classic_disorder_preview_checkbox)
    CheckBox classicCheckbox;

    @BindView(R.id.serial_disorder_preview_checkbox)
    CheckBox serialCheckbox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_side_navigation, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        classicCheckbox.setOnCheckedChangeListener(onCheckedChangeListener);
        serialCheckbox.setOnCheckedChangeListener(onCheckedChangeListener);
        classicCheckbox.setChecked(SettingManager.Instance().getBoolean(Constant.CLASSIC_DISORDER_PREVIEW, true));
        serialCheckbox.setChecked(SettingManager.Instance().getBoolean(Constant.SERIAL_DISORDER_PREVIEW, true));
    }

    @OnClick(R.id.five_star)
    public void toMarket() {
        Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.exit)
    public void exit() {
        getActivity().finish();
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView == classicCheckbox) {
                    SettingManager.Instance().setBoolean(Constant.CLASSIC_DISORDER_PREVIEW, isChecked);
                    EventBus.getDefault().post(new ClassicDisorderPreviewSettingChange());
                }
                if (buttonView == serialCheckbox) {
                    SettingManager.Instance().setBoolean(Constant.SERIAL_DISORDER_PREVIEW, isChecked);
                    EventBus.getDefault().post(new SerialDisorderPreviewSettingChange());
                }
        }
    };


}
