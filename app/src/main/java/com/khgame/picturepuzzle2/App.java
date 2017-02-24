package com.khgame.picturepuzzle2;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.khgame.picturepuzzle.common.Operation;
import com.khgame.picturepuzzle.common.SettingManager;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.db.DBManager;
import com.khgame.picturepuzzle.db.model.ClassicPicture;
import com.khgame.picturepuzzle.db.operation.InsertClassicPictureOperation;
import com.khgame.picturepuzzle2.operation.CopyAssetsToDiskOperation;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by zkang on 2017/2/18.
 */

public class App extends Application {
    public static final String TAG = "App";

    public static Application application = null;

    @Override
    public void onCreate() {
        super.onCreate();
        App.application = this;
        SettingManager.Initialize(this);
        DBManager.initialize(this);
        initializeIfFirstTime();
        initImageLoader();
    }

    private void initializeIfFirstTime() {

        int openTimes = SettingManager.Instance().getInt("OpenTimes", 0);

        if(openTimes == 0) {
            copyToDisk();
        }

        SettingManager.Instance().setInt("OpenTimes", ++openTimes);
    }

    private void copyToDisk() {

        for (int i = 1; i < 10; i++) {
            final String pictureName = "default0" + i;
            String assets = "default0" + i + ".jpg";
            File destination = new File(ClassicPictureDir(), pictureName);
            new CopyAssetsToDiskOperation(assets, destination).callback(new Operation.Callback<File, Void>() {
                @Override
                public void onSuccess(final File file) {
                    Log.d(TAG, "copyToDisk File:" + pictureName + ", Success!");
                    ClassicPicture picture = new ClassicPicture();
                    picture.localPath = file.getAbsolutePath();
                    picture.easyData = DisorderUtil.newDisorderString(GameLevel.EASY);
                    picture.mediumData = DisorderUtil.newDisorderString(GameLevel.MEDIUM);
                    picture.hardData = DisorderUtil.newDisorderString(GameLevel.HARD);

                    new InsertClassicPictureOperation(picture).callback(new Operation.Callback<ClassicPicture, Void>() {
                        @Override
                        public void onSuccess(ClassicPicture picture) {
                            Log.d(TAG, "insertToDB, File:" + file.getName() + ", Success!");
                        }

                        @Override
                        public void onFailure(Void aVoid) {
                            Log.e(TAG, "insertToDB, File:" + file.getName() + ", Failure!");
                        }
                    }).enqueue();
                }
            }).enqueue();
        }
    }

    public void initImageLoader() {
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        .taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)
        .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder(false)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static File ClassicPictureDir() {
        File dir = new File(application.getCacheDir(), "classic");
        if(!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File SerialCoverPictureDir() {
        return new File(application.getCacheDir().getAbsolutePath() + "/serial/covers");
    }


    public static File SerialPictureDir(String serialName) {
        return new File(application.getCacheDir().getAbsolutePath() + "/serial/" + serialName);
    }
}
