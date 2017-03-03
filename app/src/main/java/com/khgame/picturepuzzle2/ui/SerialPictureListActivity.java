package com.khgame.picturepuzzle2.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khgame.picturepuzzle.base.SquaredActivity;
import com.khgame.picturepuzzle.common.SettingManager;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.model.BitmapEntry;
import com.khgame.picturepuzzle.model.Serial;
import com.khgame.picturepuzzle.model.SerialPicture;
import com.khgame.picturepuzzle.operation.LoadPictureOperation;
import com.khgame.picturepuzzle.operation.Operation;
import com.khgame.picturepuzzle.serial.SerialManager;
import com.khgame.picturepuzzle.serial.SerialManagerImpl;
import com.khgame.picturepuzzle.serial.SerialPicturesLoadFinishEvent;
import com.khgame.picturepuzzle2.R;
import com.khgame.picturepuzzle2.ui.view.DisorderImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zkang on 2017/2/26.
 */

public class SerialPictureListActivity extends SquaredActivity {

    private Serial serial;
    private SerialManager serialManager = SerialManagerImpl.getInstance();

    @BindView(R.id.gridview)
    GridView gridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_picture_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(onItemClickListener);
        String uuid = getIntent().getStringExtra("uuid");
        serial = serialManager.getSerialByUuid(uuid);
        serialManager.startSerial(serial);
        toolbar.setTitle(serial.name);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused") // invoked by event bus
    public void onEventMainThread(SerialPicturesLoadFinishEvent event) {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialManager.endSerial();
    }

    @OnClick(R.id.fab)
    void onClickFab() {
        switch (serial.gameLevel) {
            case GameLevel.EASY:
                serial.gameLevel = GameLevel.MEDIUM;
                break;
            case GameLevel.MEDIUM:
                serial.gameLevel = GameLevel.HARD;
                break;
            case GameLevel.HARD:
                serial.gameLevel = GameLevel.EASY;
                break;
        }
        serialManager.updateCurrentSerial();
        adapter.notifyDataSetChanged();
    }

    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return serialManager.getCurrentSerialPictureList().size();
        }

        @Override
        public Object getItem(int i) {
            return serialManager.getCurrentSerialPictureList().get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            SerialPicture serialPicture = (SerialPicture) getItem(i);
            Context context = SerialPictureListActivity.this;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.serial_picture_item, null);

            final DisorderImageView imageView = (DisorderImageView) view.findViewById(R.id.image);
            TextView textView = (TextView) view.findViewById(R.id.title);
            imageView.setLayoutParams(getLayoutParams());
            textView.setText(serialPicture.name);
            imageView.setTag(serialPicture.uuid);
            new LoadPictureOperation(serialPicture.uuid, serialPicture.networkPath)
                    .callback(new Operation.Callback<BitmapEntry, Void>() {
                        @Override
                        public void onSuccessMainThread(BitmapEntry bitmapEntry) {
                            imageView.setBitmap(bitmapEntry.bitmap);
                        }
                    }).enqueue();

            String gameData = null;
            switch (serial.gameLevel) {
                case GameLevel.EASY:
                    gameData = serialPicture.easyData;
                    break;
                case GameLevel.MEDIUM:
                    gameData = serialPicture.mediumData;
                    break;
                case GameLevel.HARD:
                    gameData = serialPicture.hardData;
                    break;
            }
            imageView.setPositionList(DisorderUtil.decode(gameData));
            imageView.setLayoutParams(getLayoutParams());
            return view;
        }

        private LinearLayout.LayoutParams getLayoutParams() {
            android.graphics.Point point = new android.graphics.Point();
            SerialPictureListActivity.this.getWindowManager().getDefaultDisplay().getSize(point);
            int displayWidth = point.x;
            int imageW = displayWidth / 3;
            int imageH = imageW * 4 / 3;
            return new LinearLayout.LayoutParams(imageW, imageH);
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SerialPicture serialPicture = serialManager.getCurrentSerialPictureList().get(i);
            Intent intent = new Intent(SerialPictureListActivity.this, SerialGameActivity.class);
            intent.putExtra("uuid", serialPicture.uuid);
            startActivity(intent);
        }
    };

}
