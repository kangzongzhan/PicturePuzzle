package com.khgame.picturepuzzle.data.source.remote;

import com.khgame.picturepuzzle.data.ClassicPicture;
import com.khgame.picturepuzzle.data.Serial;
import com.khgame.picturepuzzle.data.SerialPicture;
import com.khgame.picturepuzzle.data.source.PictureLoader;

import java.util.List;

import io.reactivex.Flowable;

public class RemoteDataSource implements PictureLoader {
    @Override
    public Flowable<List<ClassicPicture>> getClassicPictures() {
        return RemoteServiceProvider.getInstance().getClassicService().getAllClassicPicturesRx();
    }

    @Override
    public Flowable<List<Serial>> getSerials() {
        return RemoteServiceProvider.getInstance().getSerialService().getAllSerialsRx();
    }

    @Override
    public Flowable<List<SerialPicture>> getSerialPictures(String serialId) {
        return RemoteServiceProvider.getInstance().getSerialService().getSerialPicturesRx(serialId);
    }
}
