package com.khgame.picturepuzzle.data.source.local;

import android.support.annotation.NonNull;

import com.khgame.picturepuzzle.data.ClassicPicture;
import com.khgame.picturepuzzle.data.Serial;
import com.khgame.picturepuzzle.data.SerialPicture;
import com.khgame.picturepuzzle.data.source.PictureDataSource;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class LocalDataSource implements PictureDataSource {

    private static volatile LocalDataSource INSTANCE;

    private SerialDao serialDao;
    private SerialPictureDao serialPictureDao;
    private ClassicPictureDao classicPictureDao;

    // Prevent direct instantiation.
    private LocalDataSource(@NonNull SerialDao serialDao, @NonNull SerialPictureDao serialPictureDao, @NonNull ClassicPictureDao classicPictureDao) {
        this.serialDao = serialDao;
        this.serialPictureDao = serialPictureDao;
        this.classicPictureDao = classicPictureDao;
    }

    public static LocalDataSource getInstance(@NonNull SerialDao serialDao, @NonNull SerialPictureDao serialPictureDao, @NonNull ClassicPictureDao classicPictureDao) {
        if (INSTANCE == null) {
            synchronized (LocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalDataSource(serialDao, serialPictureDao, classicPictureDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<ClassicPicture>> getClassicPictures() {
        return null;
    }

    @Override
    public Flowable<List<Serial>> getSerials() {
        return serialDao.getSerials();
    }

    @Override
    public Flowable<List<SerialPicture>> getSerialPictures(String serialId) {
        return null;
    }
}
