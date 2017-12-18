package com.khgame.picturepuzzle.data.source.remote;


import com.khgame.picturepuzzle.data.ClassicPicture;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface ClassicService {
    @GET("classics")
    Flowable<List<ClassicPicture>> getAllClassicPicturesRx();
}
