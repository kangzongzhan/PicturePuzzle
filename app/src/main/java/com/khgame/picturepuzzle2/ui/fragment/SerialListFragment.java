package com.khgame.picturepuzzle2.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khgame.picturepuzzle.base.SquaredFragment;
import com.khgame.picturepuzzle.model.BitmapEntry;
import com.khgame.picturepuzzle.model.Serial;
import com.khgame.picturepuzzle.operation.LoadPictureOperation;
import com.khgame.picturepuzzle.operation.Operation;
import com.khgame.picturepuzzle.serial.SerialInstallEvent;
import com.khgame.picturepuzzle.serial.SerialInstallManager;
import com.khgame.picturepuzzle.serial.SerialManager;
import com.khgame.picturepuzzle.serial.SerialManagerImpl;
import com.khgame.picturepuzzle.serial.SerialStateUpdateEvent;
import com.khgame.picturepuzzle.serial.SerialsUpdateEvent;
import com.khgame.picturepuzzle2.R;
import com.khgame.picturepuzzle2.ui.activity.SerialPicturesActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zkang on 2017/1/7.
 */

public class SerialListFragment extends SquaredFragment {
    private static String TAG = "SerialListFragment";

    @BindView(R.id.gridview)
    GridView gridView;
    SerialManager serialManager = SerialManagerImpl.getInstance();

    SerialListAdapter listAdapter = new SerialListAdapter();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_serial_list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView.setAdapter(listAdapter);
        serialManager.loadSerials();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused") // invoked by event bus
    public void onEventMainThread(SerialsUpdateEvent event) {
        listAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused") // invoked by event bus
    public void onEventMainThread(SerialInstallEvent event) {
        switch (event.type) {
            case END:
                listAdapter.notifyDataSetChanged();
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SerialStateUpdateEvent event) {
        SerialListAdapter.ViewHolder viewHolder = getViewHolder(event.serial);
        if (viewHolder != null) {
            viewHolder.setSerial(event.serial);
        }
    }

    private SerialListAdapter.ViewHolder getViewHolder(Serial serial) {
        int count = gridView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = gridView.getChildAt(i);
            SerialListAdapter.ViewHolder holder = (SerialListAdapter.ViewHolder) view.getTag();
            if (serial.equals(holder.getSerial())) {
                return holder;
            }
        }
        return null;
    }



    class SerialListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return serialManager.getSerials().size();
        }

        @Override
        public Object getItem(int i) {
            return serialManager.getSerials().get(i);
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
                cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
                cover.setLayoutParams(getLayoutParams());
            }
            public void setSerial(Serial serial) {
                this.serial = serial;
                new LoadPictureOperation(serial.uuid, serial.networkCoverPath)
                        .callback(new Operation.Callback<BitmapEntry, Void>() {
                            @Override
                            public void onSuccessMainThread(BitmapEntry bitmapEntry) {
                                cover.setImageBitmap(bitmapEntry.bitmap);
                            }
                        }).enqueue();

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
                if(serial.installState == Serial.State.UNINSTALL) {
                    Log.d("kzz", "开始安装");
                    SerialInstallManager.getInstance().install(serial);
                }

                if(serial.installState == Serial.State.INSTALLED) {
                    Log.d("kzz", "已经安装");
                    Intent intent = new Intent();
                    intent.setClass(getContext(), SerialPicturesActivity.class);
                    intent.putExtra("uuid", serial.uuid);
                    startActivity(intent);
                }

                if(serial.installState == Serial.State.INSTALLING) {
                    Log.d("kzz", "正在安装");
                }
            }

            public Serial getSerial() {
                return serial;
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
    }
}
