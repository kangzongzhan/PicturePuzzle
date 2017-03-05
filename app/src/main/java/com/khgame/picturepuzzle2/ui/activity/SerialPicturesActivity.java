package com.khgame.picturepuzzle2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.khgame.picturepuzzle.base.SquaredActivity;
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
import com.khgame.picturepuzzle2.ui.view.ProgressHit;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SerialPicturesActivity extends SquaredActivity {

    private Serial serial;
    private SerialManager serialManager = SerialManagerImpl.getInstance();

    @BindView(R.id.gridview)
    GridView gridView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_pictures);
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

    @Override
    protected void onResume() {
        super.onResume();
        updateFabImage();
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
        updateFabImage();
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

            View disorderView;
            ViewHolder viewHolder;
            if(view != null) {
                disorderView = view;
                viewHolder = (ViewHolder) disorderView.getTag();
            } else {
                disorderView = LayoutInflater.from(SerialPicturesActivity.this).inflate(R.layout.classic_disorder, null);
                disorderView.setLayoutParams(getLayoutParams());
                viewHolder = new ViewHolder(disorderView);
            }
            viewHolder.setSerialPicture((SerialPicture) getItem(i));
            return disorderView;
        }

        private ListView.LayoutParams getLayoutParams() {
            android.graphics.Point point = new android.graphics.Point();
            SerialPicturesActivity.this.getWindowManager().getDefaultDisplay().getSize(point);
            int displayWidth = point.x;
            int imageW = displayWidth / 3;
            int imageH = imageW * 4 / 3;
            return new ListView.LayoutParams(imageW, imageH);
        }

        class ViewHolder {
            @BindView(R.id.disorderImageView)
            DisorderImageView disorderImageView;

            @BindView(R.id.progressBar)
            ProgressHit progressHit;

            SerialPicture serialPicture;

            public ViewHolder(View view) {
                view.setTag(this);
                disorderImageView = (DisorderImageView) view.findViewById(R.id.disorderImageView);
                progressHit = (ProgressHit) view.findViewById(R.id.progressBar);
                //ButterKnife.bind(this, view);
            }

            public void setSerialPicture(final SerialPicture picture) {
                this.serialPicture = picture;
                new LoadPictureOperation(picture.uuid, picture.networkPath).callback(new Operation.Callback<BitmapEntry, Void>() {
                    @Override
                    public void onSuccessMainThread(BitmapEntry bitmapEntry) {
                        if(serialPicture.uuid.equals(bitmapEntry.uuid)) {
                            disorderImageView.setBitmap(bitmapEntry.bitmap);
                        }
                    }
                }).enqueue();

                switch (serial.gameLevel) {
                    case GameLevel.EASY:
                        disorderImageView.setPositionList(DisorderUtil.decode(picture.easyData));
                        break;
                    case GameLevel.MEDIUM:
                        disorderImageView.setPositionList(DisorderUtil.decode(picture.mediumData));
                        break;
                    case GameLevel.HARD:
                        disorderImageView.setPositionList(DisorderUtil.decode(picture.hardData));
                        break;
                }
                progressHit.setGameData(picture.easyData, picture.mediumData, picture.hardData);
            }
        }

    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SerialPicture serialPicture = serialManager.getCurrentSerialPictureList().get(i);
            Intent intent = new Intent(SerialPicturesActivity.this, SerialGameActivity.class);
            intent.putExtra("uuid", serialPicture.uuid);
            startActivity(intent);
        }
    };

    private void updateFabImage() {
        switch (serial.gameLevel) {
            case GameLevel.EASY:
                fab.setImageResource(R.drawable.ic_one);
                break;
            case GameLevel.MEDIUM:
                fab.setImageResource(R.drawable.ic_two);
                break;
            case GameLevel.HARD:
                fab.setImageResource(R.drawable.ic_three);
                break;
        }
    }
}
