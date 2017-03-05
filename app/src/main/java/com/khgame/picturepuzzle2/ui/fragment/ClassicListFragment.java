package com.khgame.picturepuzzle2.ui.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.khgame.picturepuzzle.base.SquaredFragment;
import com.khgame.picturepuzzle.model.BitmapEntry;
import com.khgame.picturepuzzle.model.ClassicPicture;
import com.khgame.picturepuzzle.operation.CopyUriPicture;
import com.khgame.picturepuzzle.operation.LoadPictureOperation;
import com.khgame.picturepuzzle.operation.Operation;
import com.khgame.picturepuzzle.common.SettingManager;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.db.operation.QueryAllClassicPicturesOperation;
import com.khgame.picturepuzzle2.App;
import com.khgame.picturepuzzle2.R;
import com.khgame.picturepuzzle2.ui.activity.ClassicGameActivity;
import com.khgame.picturepuzzle2.ui.view.DisorderImageView;
import com.khgame.picturepuzzle2.ui.view.ProgressHit;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zkang on 2017/1/7.
 */

public class ClassicListFragment extends SquaredFragment {

    private static final String TAG = "ClassicListFragment";
    private int gameLevel = GameLevel.EASY;

    @BindView(R.id.gridview)
    GridView gridView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    Uri takePhotoUri;
    Uri selectGalleryUri;

    private static final int REQUEST_TAKE_PHOTO = 100;
    private static final int REQUEST_GET_CONTENT = 103;

    private static final int RESULT_OK = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        gameLevel = SettingManager.Instance().getInt("ClassicLevel", GameLevel.EASY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classic_picture_list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        gridView.setAdapter(listAdapter);
        gridView.setOnItemClickListener(onItemClickListener);
        updateFabImage();
    }

    @Override
    public void onResume() {
        super.onResume();
        listAdapter.loadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_classic_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_gallery:
                selectGallery();
                return true;
            case R.id.action_take_photo:
                takePhoto();
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Uri destinationUri = Uri.fromFile(App.PictureFile(UUID.randomUUID().toString()));
            startClipActivity(takePhotoUri, destinationUri);
        }
        if (requestCode == REQUEST_GET_CONTENT && resultCode == RESULT_OK) {
            selectGalleryUri = data.getData();
            Uri destinationUri = Uri.fromFile(App.PictureFile(UUID.randomUUID().toString()));
            startClipActivity(selectGalleryUri, destinationUri);
        }

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            final Uri resultUri = UCrop.getOutput(data);
            new CopyUriPicture(resultUri).callback(new Operation.Callback<ClassicPicture, Void>() {
                @Override
                public void onSuccessMainThread(ClassicPicture classicPicture) {
                    listAdapter.loadData();
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ClassicGameActivity.class);
                    intent.putExtra("GameLevel", gameLevel);
                    intent.putExtra("uuid", classicPicture.uuid);
                    startActivity(intent);
                }

                @Override
                public void onFailureMainThread(Void aVoid) {
                    Log.d("kzz", "copy uri failure");
                    super.onFailureMainThread(aVoid);
                }
            }).enqueue();
        }

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
        updateFabImage();
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

            View disorderView;
            ViewHolder viewHolder;
            if(view != null) {
                disorderView = view;
                viewHolder = (ViewHolder) disorderView.getTag();
            } else {
                disorderView = LayoutInflater.from(ClassicListFragment.this.getContext()).inflate(R.layout.classic_disorder, null);
                disorderView.setLayoutParams(getLayoutParams());
                viewHolder = new ViewHolder(disorderView);
            }
            viewHolder.setClassicPicture(pictures.get(i));
            return disorderView;
        }

        private ViewGroup.LayoutParams getLayoutParams() {
            Point point = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(point);
            int displayWidth = point.x;
            int imageW = displayWidth / 3;
            int imageH = imageW * 4 / 3;
            return new ListView.LayoutParams(imageW, imageH);
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

        class ViewHolder {
            @BindView(R.id.disorderImageView)
            DisorderImageView disorderImageView;

            @BindView(R.id.progressBar)
            ProgressHit progressHit;

            ClassicPicture classicPicture;

            public ViewHolder(View view) {
                view.setTag(this);
                ButterKnife.bind(this, view);
            }

            public void setClassicPicture(final ClassicPicture picture) {
                this.classicPicture = picture;
                new LoadPictureOperation(picture.uuid, picture.networkPath).callback(new Operation.Callback<BitmapEntry, Void>() {
                    @Override
                    public void onSuccessMainThread(BitmapEntry bitmapEntry) {
                        if(classicPicture.uuid.equals(bitmapEntry.uuid)) {
                            disorderImageView.setBitmap(bitmapEntry.bitmap);
                        }
                    }
                }).enqueue();

                switch (gameLevel) {
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
    }

    private ClassicListAdapter listAdapter = new ClassicListAdapter();
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ClassicPicture picture = (ClassicPicture) listAdapter.getItem(position);
            Intent intent = new Intent();
            intent.setClass(getActivity(), ClassicGameActivity.class);
            intent.putExtra("GameLevel", gameLevel);
            intent.putExtra("uuid", picture.uuid);
            startActivity(intent);
        }
    };

    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GET_CONTENT);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            startActivityForResult(intent, SELECT_PIC_KITKAT);
//        } else {
//            startActivityForResult(intent, IMAGE_REQUEST_CODE);
//        }
    }
    private void takePhoto() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File outputImage = new File(path, UUID.randomUUID().toString());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, outputImage.getAbsolutePath());
        takePhotoUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoUri);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void startClipActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(3, 4)
                .withMaxResultSize(600, 800)
                .start(getContext(), this);
    }

    private void updateFabImage() {
        switch (gameLevel) {
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
