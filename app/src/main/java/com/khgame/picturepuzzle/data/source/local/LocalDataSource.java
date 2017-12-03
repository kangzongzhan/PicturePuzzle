package com.khgame.picturepuzzle.data.source.local;

import android.support.annotation.NonNull;

import com.khgame.picturepuzzle.data.ClassicPicture;
import com.khgame.picturepuzzle.data.Serial;
import com.khgame.picturepuzzle.data.SerialPicture;
import com.khgame.picturepuzzle.data.source.PictureDataSource;

import java.util.List;

import io.reactivex.Observable;

public class LocalDataSource implements PictureDataSource {

    private static volatile LocalDataSource INSTANCE;

    private SerialDao serialDao;

    // Prevent direct instantiation.
    private LocalDataSource(@NonNull SerialDao serialDao) {
        this.serialDao = serialDao;
    }

    public static LocalDataSource getInstance(@NonNull SerialDao serialDao) {
        if (INSTANCE == null) {
            synchronized (LocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalDataSource(serialDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<ClassicPicture>> getClassicPictures() {
        return null;
    }

    @Override
    public Observable<List<Serial>> getSerials() {
        return serialDao.getSerials();
    }

    @Override
    public Observable<List<SerialPicture>> getSerialPictures(String serialId) {
        return null;
    }
}
