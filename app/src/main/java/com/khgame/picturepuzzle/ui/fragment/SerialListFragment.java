package com.khgame.picturepuzzle.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khgame.sdk.picturepuzzle.base.SquaredFragment;
import com.khgame.sdk.picturepuzzle.common.BitmapManager;
import com.khgame.sdk.picturepuzzle.common.BitmapManagerImpl;
import com.khgame.sdk.picturepuzzle.common.Result;
import com.khgame.sdk.picturepuzzle.events.BitmapLoadEvent;
import com.khgame.sdk.picturepuzzle.events.SerialFilterPassEvent;
import com.khgame.sdk.picturepuzzle.model.Serial;
import com.khgame.sdk.picturepuzzle.serial.SerialInstallEvent;
import com.khgame.sdk.picturepuzzle.serial.SerialManager;
import com.khgame.sdk.picturepuzzle.serial.SerialManagerImpl;
import com.khgame.sdk.picturepuzzle.serial.SerialsLoadEvent;
import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.ui.activity.SerialPictureListActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zkang on 2017/1/7.
 */

public class SerialListFragment extends SquaredFragment {
    private static final String COMPONENT = SerialListFragment.class.getSimpleName();

    @BindView(R.id.gridview)
    GridView gridView;
    SerialListAdapter listAdapter = new SerialListAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LifeCycle", COMPONENT + " - onCreateView");
        return inflater.inflate(R.layout.fragment_serial_list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("LifeCycle", COMPONENT + " - onViewCreated");
        gridView.setAdapter(listAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("LifeCycle", COMPONENT + " - onResume");
        listAdapter.loadSerials();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("LifeCycle", COMPONENT + " - onPause");

    }

    class SerialListAdapter extends BaseAdapter {
        SerialManager serialManager = SerialManagerImpl.getInstance();
        BitmapManager bitmapManager = BitmapManagerImpl.getInstance();

        private List<Serial> serials = new ArrayList<>();

        public SerialListAdapter() {
            EventBus.getDefault().register(this);
        }
        public void loadSerials() {
            serialManager.loadSerials();
        }
        @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
        public void onEventMainThread(SerialsLoadEvent event) {
            if (event.result == Result.Success) {
                serials = event.serials;
                this.notifyDataSetChanged();
            }
        }

        @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
        public void onEventMainThread(SerialInstallEvent event) {
            Serial serial = getSerialByUuid(event.serial.uuid);
            switch (event.type) {
                case BEGIN:
                    Log.d("InstallSerial", "BEGIN - SerialName:" + event.serial.name);
                    serial.installState = Serial.State.INSTALLING;
                    serial.installProgress = 0;
                    break;
                case END:
                    Log.d("InstallSerial", "END - SerialName:" + event.serial.name);
                    serial.installState = Serial.State.INSTALLED;
                    serial.installProgress = 100;
                    break;
                case INSTALLING:
                    Log.d("InstallSerial", "INSTALLING - SerialName:" + event.serial.name + ", Progress:" + event.progress);
                    serial.installState = Serial.State.INSTALLING;
                    serial.installProgress = event.progress;
                    break;
            }
            this.notifyDataSetChanged();
        }

        @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
        public void onEventMainThread(SerialFilterPassEvent event) {
            serialManager.setPassedReview();
            serialManager.loadSerials();
        }
        @Override
        public int getCount() {
            return serials.size();
        }

        @Override
        public Object getItem(int i) {
            return serials.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                view = LayoutInflater.from(SerialListFragment.this.getContext()).inflate(R.layout.serial_item, null);
                viewHolder = new ViewHolder(view);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Serial serial = (Serial) getItem(i);
            viewHolder.setSerial(serial);
            return view;
        }

        class ViewHolder implements View.OnClickListener {
            @BindView(R.id.image) ImageView cover;
            @BindView(R.id.title) TextView title;

            private Serial serial;
            public ViewHolder(View view) {
                view.setTag(this);
                view.setOnClickListener(this);
                ButterKnife.bind(this, view);
                bus.register(this);
                cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
                cover.setLayoutParams(getLayoutParams());
            }
            public void setSerial(Serial serial) {
                this.serial = serial;
                bitmapManager.loadBitmapByUuid(serial.uuid);

                if (serial.installState == Serial.State.UNINSTALL) {
                    cover.setAlpha(0.5f);
                    title.setText(serial.name);
                }
                if (serial.installState == Serial.State.INSTALLED) {
                    cover.setAlpha(1f);
                    title.setText(serial.name);
                }
                if (serial.installState == Serial.State.INSTALLING) {
                    float alpha = 0.5f + serial.installProgress / 200f;
                    cover.setAlpha(alpha);
                    title.setText(serial.installProgress + "%");
                }
            }

            @Override
            public void onClick(View v) {

                if (serial.installState == Serial.State.UNINSTALL) {
                    Log.d("kzz", "开始安装");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.install_serial_hint);
                    builder.setPositiveButton(android.R.string.yes, (dialog, which) -> serialManager.install(serial));
                    builder.setNegativeButton(android.R.string.no, null);
                    builder.create().show();
                }

                if (serial.installState == Serial.State.INSTALLED) {
                    Log.d("kzz", "已经安装");
                    Intent intent = new Intent();
                    intent.setClass(getContext(), SerialPictureListActivity.class);
                    intent.putExtra(SerialPictureListActivity.SERIAL_UUID, serial.uuid);
                    intent.putExtra(SerialPictureListActivity.SERIAL_PRIMARY_COLOR, serial.primaryColor);
                    intent.putExtra(SerialPictureListActivity.SERIAL_SECONDARY_COLOR, serial.secondaryColor);
                    startActivity(intent);
                }

                if (serial.installState == Serial.State.INSTALLING) {
                    Log.d("kzz", "正在安装");
                }
            }

            @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
            public void onEventMainThread(BitmapLoadEvent event) {
                if (event.result == Result.Success && TextUtils.equals(event.uuid, serial.uuid)) {
                    cover.setImageBitmap(event.bitmap);
                }
            }
        }

        private LinearLayout.LayoutParams getLayoutParams() {
            android.graphics.Point point = new android.graphics.Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(point);
            int displayWidth = point.x;
            int imageW = displayWidth / 3;
            int imageH = imageW * 4 / 3;
            return new LinearLayout.LayoutParams(imageW, imageH);
        }

        private Serial getSerialByUuid(String uuid) {
            for (Serial serial:serials) {
                if (serial.uuid.equals(uuid)) {
                    return serial;
                }
            }
            return null;
        }
    }
}
