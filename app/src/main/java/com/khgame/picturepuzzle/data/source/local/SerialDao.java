package com.khgame.picturepuzzle.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.khgame.picturepuzzle.data.Serial;

import java.util.List;

import io.reactivex.Flowable;
@Dao
public interface SerialDao {

    @Query("SELECT * FROM Serial")
    Flowable<List<Serial>> getSerials();
}
