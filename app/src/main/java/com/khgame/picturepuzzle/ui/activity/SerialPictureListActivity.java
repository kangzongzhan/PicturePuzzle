package com.khgame.picturepuzzle.ui.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khgame.sdk.picturepuzzle.base.SquaredActivity;
import com.khgame.sdk.picturepuzzle.common.BitmapManager;
import com.khgame.sdk.picturepuzzle.common.BitmapManagerImpl;
import com.khgame.sdk.picturepuzzle.common.Constant;
import com.khgame.sdk.picturepuzzle.common.Result;
import com.khgame.sdk.picturepuzzle.common.SettingManager;
import com.khgame.sdk.picturepuzzle.core.DisorderUtil;
import com.khgame.sdk.picturepuzzle.core.GameLevel;
import com.khgame.sdk.picturepuzzle.events.BitmapLoadEvent;
import com.khgame.sdk.picturepuzzle.events.SerialDisorderPreviewSettingChange;
import com.khgame.sdk.picturepuzzle.model.Serial;
import com.khgame.sdk.picturepuzzle.model.SerialPicture;
import com.khgame.sdk.picturepuzzle.serial.SerialLoadEvent;
import com.khgame.sdk.picturepuzzle.serial.SerialManager;
import com.khgame.sdk.picturepuzzle.serial.SerialManagerImpl;
import com.khgame.sdk.picturepuzzle.serial.SerialPictureManager;
import com.khgame.sdk.picturepuzzle.serial.SerialPictureManagerImpl;
import com.khgame.sdk.picturepuzzle.serial.SerialPicturesLoadEvent;
import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.ui.view.DisorderImageView;
import com.khgame.picturepuzzle.ui.view.ProgressHit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SerialPictureListActivity extends SquaredActivity {

    public static final String SERIAL_UUID = "SERIAL_UUID";
    public static final String SERIAL_PRIMARY_COLOR = "SERIAL_PRIMARY_COLOR";
    public static final String SERIAL_SECONDARY_COLOR = "SERIAL_SECONDARY_COLOR";

    private String uuid;
    private int primaryColor;
    private int secondaryColor;
    private Serial serial;
    private SerialManager serialManager = SerialManagerImpl.getInstance();
    private BitmapManager bitmapManager = BitmapManagerImpl.getInstance();
    private SerialPictureListAdapter adapter = new SerialPictureListAdapter();

    @BindView(R.id.gridview)
    GridView gridView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_pictures);
        ButterKnife.bind(this);

        uuid = getIntent().getStringExtra(SerialPictureListActivity.SERIAL_UUID);
        primaryColor = getIntent().getIntExtra(SerialPictureListActivity.SERIAL_PRIMARY_COLOR, getResources().getColor(R.color.colorPrimary));
        secondaryColor = getIntent().getIntExtra(SerialPictureListActivity.SERIAL_SECONDARY_COLOR, getResources().getColor(R.color.colorAccent));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateTheme(primaryColor, secondaryColor);

        gridView.setAdapter(adapter);

        serialManager.getSerialBySerialUuid(uuid);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.loadSerialPictures();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFabImage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
    public void onEventMainThread(SerialLoadEvent event) {
        if (event.result == Result.Success && event.serial.uuid.equals(uuid)) {
            Log.d("EventMainThread", "SerialLoadEvent");
            serial = event.serial;

            getSupportActionBar().setTitle(serial.name);

            primaryColor = serial.primaryColor;
            secondaryColor = serial.secondaryColor;
            updateTheme(primaryColor, secondaryColor);
            updateFabImage();
            adapter.notifyDataSetChanged(); // we know serial game level, do fill this list
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        serialManager.updateSerial(serial);
        if (SettingManager.Instance().getBoolean(Constant.SERIAL_DISORDER_PREVIEW, true)) {
            adapter.notifyDataSetChanged();
        }
    }


    class SerialPictureListAdapter extends BaseAdapter {
        private SerialPictureManager serialPictureManager = SerialPictureManagerImpl.getInstance();
        private List<SerialPicture> serialPictures = new ArrayList<>();

        public SerialPictureListAdapter() {
            EventBus.getDefault().register(this);
        }

        public void loadSerialPictures() {
            serialPictureManager.loadSerialPicturesBySerialUuid(uuid);
        }

        @Override
        public int getCount() {
            return serialPictures.size();
        }

        @Override
        public Object getItem(int i) {
            return serialPictures.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View disorderView;
            ViewHolder viewHolder;
            if (view != null) {
                disorderView = view;
                viewHolder = (ViewHolder) disorderView.getTag();
            } else {
                disorderView = LayoutInflater.from(SerialPictureListActivity.this).inflate(R.layout.serial_picture_item, null);
                viewHolder = new ViewHolder(disorderView);
            }
            viewHolder.setSerialPicture((SerialPicture) getItem(i));
            return disorderView;
        }

        @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
        public void onEventMainThread(SerialPicturesLoadEvent event) {
            if (event.result == Result.Success && event.serialUuid.equals(uuid)) {
                this.serialPictures = event.serialPictures;
                if (serial != null) { // we should know serial game level before fill list
                    this.notifyDataSetChanged();
                }
            }
        }

        class ViewHolder implements View.OnClickListener {
            @BindView(R.id.imageContainer)
            FrameLayout imageContainer;

            @BindView(R.id.disorderImageView)
            DisorderImageView disorderImageView;

            @BindView(R.id.orderImageView)
            ImageView orderImageView;

            @BindView(R.id.progressBar)
            ProgressHit progressHit;

            @BindView(R.id.title)
            TextView title;

            SerialPicture serialPicture;

            public ViewHolder(View view) {
                view.setTag(this);
                view.setOnClickListener(this);
                ButterKnife.bind(this, view);
                bus.register(this);
                imageContainer.setLayoutParams(getLayoutParams());
                if (SettingManager.Instance().getBoolean(Constant.SERIAL_DISORDER_PREVIEW, true)) {
                    disorderImageView.setVisibility(View.VISIBLE);
                    orderImageView.setVisibility(View.GONE);
                } else {
                    disorderImageView.setVisibility(View.GONE);
                    orderImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SerialPictureListActivity.this, SerialGameActivity.class);
                intent.putExtra(SerialGameActivity.SRIALPICTURE_UUID, serialPicture.uuid);
                intent.putExtra(SerialGameActivity.GAME_LEVEL, serial.gameLevel);
                intent.putExtra(SerialGameActivity.SRIALPICTURE_PRIMARY_COLOR, primaryColor);
                intent.putExtra(SerialGameActivity.SRIALPICTURE_SECONDARY_COLOR, secondaryColor);
                startActivity(intent);
            }

            @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
            public void onEventMainThread(BitmapLoadEvent event) {
                if (event.result == Result.Success && event.uuid.equals(serialPicture.uuid)) {
                    disorderImageView.setBitmap(event.bitmap);
                    orderImageView.setImageBitmap(event.bitmap);
                }
            }

            @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
            public void onEventMainThread(SerialDisorderPreviewSettingChange e) {
                if (SettingManager.Instance().getBoolean(Constant.SERIAL_DISORDER_PREVIEW, true)) {
                    disorderImageView.setVisibility(View.VISIBLE);
                    orderImageView.setVisibility(View.GONE);
                } else {
                    disorderImageView.setVisibility(View.GONE);
                    orderImageView.setVisibility(View.VISIBLE);
                }
            }

            public void setSerialPicture(final SerialPicture picture) {
                this.serialPicture = picture;
                disorderImageView.setBitmap(null);
                orderImageView.setImageBitmap(null);
                bitmapManager.loadBitmapByUuid(picture.uuid);
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
                title.setText(picture.name);
            }

            private LinearLayout.LayoutParams getLayoutParams() {
                android.graphics.Point point = new android.graphics.Point();
                SerialPictureListActivity.this.getWindowManager().getDefaultDisplay().getSize(point);
                int displayWidth = point.x;
                int imageW = displayWidth / 3;
                int imageH = imageW * 4 / 3;
                return new LinearLayout.LayoutParams(imageW, imageH);
            }

        }

    }

    private void updateTheme(int primaryColor, int secondaryColor) {
        setWindowStatusBarColor(primaryColor);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(primaryColor));
        fab.setBackgroundTintList(ColorStateList.valueOf(secondaryColor));
    }
    private void updateFabImage() {
        if (serial == null) {
            return;
        }
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
