package com.khgame.picturepuzzle2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.khgame.picturepuzzle.model.ClassicPicture;
import com.khgame.picturepuzzle.operation.Operation;
import com.khgame.picturepuzzle.common.SettingManager;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.db.operation.QueryAllClassicPicturesOperation;
import com.khgame.picturepuzzle2.ui.view.DisorderImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zkang on 2017/1/7.
 */

public class ClassicListFragment extends AbstractListFragment {

    private static final String TAG = "ClassicListFragment";
    private int gameLevel = GameLevel.EASY;

    @BindView(R.id.gridview)
    GridView gridView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameLevel = SettingManager.Instance().getInt("ClassicLevel", GameLevel.EASY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.three_col_listview, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        gridView.setAdapter(listAdapter);
        gridView.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        listAdapter.loadData();
    }

    @OnClick(R.id.fab)
    void onClickFab() {
        switch (gameLevel) {
            case GameLevel.EASY:
                gameLevel = GameLevel.MEDIUM;
                break;
            case GameLevel.MEDIUM:
                gameLevel = GameLevel.HARD;
                break;
            case GameLevel.HARD:
                gameLevel = GameLevel.EASY;
                break;
        }
        SettingManager.Instance().setInt("ClassicLevel", gameLevel);
        listAdapter.notifyDataSetChanged();
    }

    /**
     * classic list view adapter
     */
    class ClassicListAdapter extends BaseAdapter {

        List<ClassicPicture> pictures = new ArrayList<>();
        @Override
        public int getCount() {
            return pictures.size();
        }

        @Override
        public Object getItem(int i) {
            return pictures.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Bitmap bitmap = BitmapFactory.decodeFile(App.PictureFile(pictures.get(i).uuid).getAbsolutePath());
            DisorderImageView imageView = new DisorderImageView(ClassicListFragment.this.getContext());

            String gameData = null;
            switch (gameLevel) {
                case GameLevel.EASY:
                    gameData = pictures.get(i).easyData;
                    break;
                case GameLevel.MEDIUM:
                    gameData = pictures.get(i).mediumData;
                    break;
                case GameLevel.HARD:
                    gameData = pictures.get(i).hardData;
                    break;
            }
            imageView.setPositionList(DisorderUtil.decode(gameData));
            imageView.setBitmap(bitmap);
            imageView.setLayoutParams(getLayoutParams());
            return imageView;
        }

        private ViewGroup.LayoutParams getLayoutParams() {
            Point point = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(point);
            int displayWidth = point.x;
            int imageW = displayWidth / 3;
            int imageH = imageW * 4 / 3;
            return new ViewGroup.LayoutParams(imageW, imageH);
        }

        public void loadData() {
            new QueryAllClassicPicturesOperation().callback(new Operation.Callback<List, Void>() {
                public void onSuccessMainThread(List list) {
                    Log.d(TAG, "Query all classic picture success, total size:" + list.size());
                    ClassicListAdapter.this.pictures = list;
                    ClassicListAdapter.this.notifyDataSetChanged();
                }
            }).enqueue();
        }

    }

    private ClassicListAdapter listAdapter = new ClassicListAdapter();
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ClassicPicture picture = (ClassicPicture) listAdapter.getItem(position);
            Intent intent = new Intent();
            intent.setClass(getActivity(), ClassicGameActivity.class);
            intent.putExtra("GameLevel", gameLevel);
            intent.putExtra("ID", picture._id);
            startActivity(intent);
        }
    };
}
