package com.khgame.picturepuzzle.data.source;

import com.khgame.picturepuzzle.data.ClassicPicture;
import com.khgame.picturepuzzle.data.Serial;
import com.khgame.picturepuzzle.data.SerialPicture;

import java.util.List;

import io.reactivex.Observable;

public interface PictureDataSource {
    Observable<List<ClassicPicture>> getClassicPictures();
    Observable<List<Serial>> getSerials();
    Observable<List<SerialPicture>> getSerialPictures(String serialId);
}
