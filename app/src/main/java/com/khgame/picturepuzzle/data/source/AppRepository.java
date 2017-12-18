package com.khgame.picturepuzzle.data.source;


import com.khgame.picturepuzzle.App;
import com.khgame.picturepuzzle.data.ClassicPicture;
import com.khgame.picturepuzzle.data.Serial;
import com.khgame.picturepuzzle.data.SerialPicture;
import com.khgame.picturepuzzle.data.source.local.LocalDataSource;
import com.khgame.picturepuzzle.data.source.remote.RemoteDataSource;

import java.util.List;

import io.reactivex.Flowable;

public class AppRepository implements PictureDataSource {
    private static volatile AppRepository INSTANCE = null;
    private static LocalDataSource localDataSource;
    private static RemoteDataSource remoteDataSource;

    private AppRepository() {
    }

    public static AppRepository getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppRepository();
                    localDataSource = LocalDataSource.getInstance(App.getAppDatabase().serialDao(), App.getAppDatabase().serialPictureDao(), App.getAppDatabase().classicPictureDao());
                    remoteDataSource = new RemoteDataSource();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<ClassicPicture>> getClassicPictures() {
        return remoteDataSource.getClassicPictures();
    }

    @Override
    public Flowable<List<Serial>> getSerials() {
        return remoteDataSource.getSerials();
    }

    @Override
    public Flowable<List<SerialPicture>> getSerialPictures(String serialId) {
        return remoteDataSource.getSerialPictures(serialId);
    }
}