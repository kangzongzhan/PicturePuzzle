package com.khgame.picturepuzzle.data.source.remote;

import com.khgame.picturepuzzle.data.Serial;
import com.khgame.picturepuzzle.data.SerialPicture;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SerialService {

    @GET("serials")
    Call<List<Serial>> getAllSerials();

    @GET("serials")
    Observable<List<Serial>> getAllSerialsRx();

    @GET("serial/{serialId}")
    Call<List<SerialPicture>> getSerialPictures(@Path(value = "serialId") String id);

    @GET("serial/{serialId}")
    Observable<List<SerialPicture>> getSerialPicturesRx(@Path(value = "serialId") String id);

}
