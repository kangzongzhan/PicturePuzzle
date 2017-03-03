package com.khgame.picturepuzzle.base;

import android.os.AsyncTask;

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

import retrofit2.Retrofit;

/**
 * Created by zkang on 2017/2/24.
 */

public abstract class Application extends android.app.Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initImageLoader();
    }

    public static Application getInstance() {
        return instance;
    }


    public static File PictureDir(String uuid) {
        String str = uuid.substring(0, 2);
        File dir = new File(instance.getCacheDir(), str);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
    public static File PictureFile(String uuid) {
        return new File(PictureDir(uuid), uuid);
    }


    public static File ClassicPictureDir() {
        File dir = new File(instance.getCacheDir(), "classic");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File SerialCoverPictureDir() {
        File dir = new File(instance.getCacheDir().getAbsolutePath() + "/serial/covers");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }


    public static File SerialPictureDir(String serialName) {
        File dir = new File(instance.getCacheDir().getAbsolutePath() + "/serial/" + serialName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
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

}