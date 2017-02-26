package com.khgame.picturepuzzle2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.khgame.picturepuzzle.operation.InstallSerialOperation;
import com.khgame.picturepuzzle.operation.LoadPictureOperation;
import com.khgame.picturepuzzle.operation.Operation;
import com.khgame.picturepuzzle.serial.SerialInstallEvent;
import com.khgame.picturepuzzle.serial.SerialManager;
import com.khgame.picturepuzzle.serial.SerialManagerImpl;
import com.khgame.picturepuzzle.serial.SerialsUpdateEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

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
        return inflater.inflate(R.layout.three_col_listview, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView.setAdapter(listAdapter);
        gridView.setOnItemClickListener(onItemClickListener);
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

            Serial serial = serialManager.getSerials().get(i);
            Context context = SerialListFragment.this.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.serial_item, null);

            final ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView textView = (TextView) view.findViewById(R.id.title);
            imageView.setLayoutParams(getLayoutParams());
            textView.setText(serial.name);
            imageView.setTag(serial.uuid);
            new LoadPictureOperation(serial.uuid, serial.networkCoverPath)
                    .callback(new Operation.Callback<BitmapEntry, Void>() {
                        @Override
                        public void onSuccessMainThread(BitmapEntry bitmapEntry) {
                             imageView.setImageBitmap(bitmapEntry.bitmap);
                        }
                    }).enqueue();

            if(serial.installed.equals(Serial.SerialState.UNINSTALL)) {
                view.setAlpha(0.5f);
            }
            return view;
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

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Serial serial = (Serial) listAdapter.getItem(position);

            if(serial.installed.equals(Serial.SerialState.UNINSTALL)) {
                Log.d("kzz", "开始安装");
                serial.installed = Serial.SerialState.INSTALLING;
                serialManager.install(serial);
            }

            if(serial.installed.equals(Serial.SerialState.INSTALLED)) {
                Log.d("kzz", "已经安装");
                Intent intent = new Intent();
                intent.setClass(getContext(), SerialListActivity.class);
                intent.putExtra("uuid", serial.uuid);
                startActivity(intent);
            }

            if(serial.installed.equals(Serial.SerialState.INSTALLING)) {
                Log.d("kzz", "正在安装");
            }
        }
    };
}
