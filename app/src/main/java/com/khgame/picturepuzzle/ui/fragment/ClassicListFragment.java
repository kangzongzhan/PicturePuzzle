package com.khgame.picturepuzzle.ui.fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.khgame.sdk.picturepuzzle.base.SquaredFragment;
import com.khgame.sdk.picturepuzzle.classic.ClassicPictureManager;
import com.khgame.sdk.picturepuzzle.classic.ClassicPictureManagerImpl;
import com.khgame.sdk.picturepuzzle.common.BitmapManager;
import com.khgame.sdk.picturepuzzle.common.BitmapManagerImpl;
import com.khgame.sdk.picturepuzzle.common.Result;
import com.khgame.sdk.picturepuzzle.db.model.ClassicPicturePo;
import com.khgame.sdk.picturepuzzle.db.operation.DeleteClassicPictureByUuid;
import com.khgame.sdk.picturepuzzle.events.BitmapLoadEvent;
import com.khgame.sdk.picturepuzzle.events.ClassicPicturesLoadEvent;
import com.khgame.sdk.picturepuzzle.model.ClassicPicture;
import com.khgame.sdk.picturepuzzle.operation.CopyUriPicture;
import com.khgame.sdk.picturepuzzle.operation.LoadPictureOperation;
import com.khgame.sdk.picturepuzzle.operation.Operation;
import com.khgame.sdk.picturepuzzle.common.SettingManager;
import com.khgame.sdk.picturepuzzle.core.DisorderUtil;
import com.khgame.sdk.picturepuzzle.core.GameLevel;
import com.khgame.sdk.picturepuzzle.db.operation.QueryAllClassicPicturesOperation;
import com.khgame.picturepuzzle.App;
import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.ui.activity.ClassicGameActivity;
import com.khgame.picturepuzzle.ui.view.DisorderImageView;
import com.khgame.picturepuzzle.ui.view.ProgressHit;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by zkang on 2017/1/7.
 */

public class ClassicListFragment extends SquaredFragment implements EasyPermissions.PermissionCallbacks {

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

    private static final int PERMISSION_TAKE_PHOTO = 201;
    private static final int PERMISSION_GALLERY = 202;

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
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        gridView.setAdapter(listAdapter);
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
                    intent.putExtra(ClassicGameActivity.GAME_LEVEL, gameLevel);
                    intent.putExtra(ClassicGameActivity.CLASSICPICTURE_UUID, classicPicture.uuid);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted, requestCode:" + requestCode);
        if (requestCode == PERMISSION_TAKE_PHOTO) {
            takePhoto();
        }
        if (requestCode == PERMISSION_GALLERY) {
            selectGallery();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied, requestCode:" + requestCode);
        Toast.makeText(getContext(), "Seems do not get your permission", Toast.LENGTH_LONG).show();
    }

    /**
     * classic list view adapter
     */
    class ClassicListAdapter extends BaseAdapter  {
        ClassicPictureManager classicPictureManager = ClassicPictureManagerImpl.getInstance();
        BitmapManager bitmapManager = BitmapManagerImpl.getInstance();
        List<ClassicPicture> classicPictures = new ArrayList<>();

        public ClassicListAdapter() {
            bus.register(this);
        }

        public void loadData() {
            classicPictureManager.getAllClassicPictures();
        }

        @Override
        public int getCount() {
            return classicPictures.size();
        }

        @Override
        public Object getItem(int i) {
            return classicPictures.get(i);
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
            viewHolder.setClassicPicture(classicPictures.get(i));
            return disorderView;
        }

        @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
        public void onEventMainThread(ClassicPicturesLoadEvent event) {
            Log.d("Event-MainThread", TAG + " - ClassicPicturesLoadEvent");
            if (event.result == Result.Success) {
                classicPictures = event.classicPictures;
                this.notifyDataSetChanged();
            }
        }

        private ViewGroup.LayoutParams getLayoutParams() {
            Point point = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(point);
            int displayWidth = point.x;
            int imageW = displayWidth / 3;
            int imageH = imageW * 4 / 3;
            return new ListView.LayoutParams(imageW, imageH);
        }

        class ViewHolder implements View.OnLongClickListener, View.OnClickListener{
            @BindView(R.id.disorderImageView)
            DisorderImageView disorderImageView;

            @BindView(R.id.progressBar)
            ProgressHit progressHit;

            ClassicPicture classicPicture;

            public ViewHolder(View view) {
                view.setTag(this);
                view.setOnClickListener(this);
                view.setOnLongClickListener(this);
                ButterKnife.bind(this, view);
                bus.register(this);
            }

            public void setClassicPicture(final ClassicPicture picture) {
                this.classicPicture = picture;
                bitmapManager.loadBitmapByUuid(picture.uuid);
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

            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClassicListFragment.this.getContext());
                builder.setMessage(R.string.delete_hint)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                new DeleteClassicPictureByUuid(classicPicture.uuid).callback(new Operation.Callback<Void, Void>() {
                                    @Override
                                    public void onSuccessMainThread(Void aVoid) {
                                        loadData();
                                    }
                                }).enqueue();
                        })
                        .setNegativeButton(android.R.string.no, null);
                builder.create().show();
                return true;
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ClassicGameActivity.class);
                intent.putExtra(ClassicGameActivity.CLASSICPICTURE_UUID, classicPicture.uuid);
                intent.putExtra(ClassicGameActivity.GAME_LEVEL, gameLevel);
                startActivity(intent);
            }

            @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
            public void onEventMainThread(BitmapLoadEvent event) {
                if (event.result == Result.Success && TextUtils.equals(event.uuid, classicPicture.uuid)) {
                    disorderImageView.setBitmap(event.bitmap);
                }
            }
        }
    }

    private ClassicListAdapter listAdapter = new ClassicListAdapter();


    private void selectGallery() {
        String perm = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (!EasyPermissions.hasPermissions(getContext(), perm)) {
            Log.d(TAG, "selectGallery request permission, permission code:" + PERMISSION_GALLERY);
            EasyPermissions.requestPermissions(this, "Select gallery need your permission", PERMISSION_GALLERY, perm);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GET_CONTENT);
    }

    void takePhoto() {
        String[] perms = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(getContext(), perms)) {
            Log.d(TAG, "takePhoto request permission, permission code:" + PERMISSION_GALLERY);
            EasyPermissions.requestPermissions(this, "Take photo need your permission", PERMISSION_TAKE_PHOTO, perms);
            return;
        }

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
