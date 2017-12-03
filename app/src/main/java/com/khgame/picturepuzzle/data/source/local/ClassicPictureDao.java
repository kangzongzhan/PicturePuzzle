package com.khgame.picturepuzzle.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.khgame.picturepuzzle.data.ClassicPicture;

import java.util.List;

@Dao
public interface ClassicPictureDao {
    @Query("SELECT * FROM ClassicPicture")
    List<ClassicPicture> getClassics();

    /**
     * Insert a classic picture in the database. If the picture already exists, replace it.
     *
     * @param classicPicture the picture to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(ClassicPicture classicPicture);
}
