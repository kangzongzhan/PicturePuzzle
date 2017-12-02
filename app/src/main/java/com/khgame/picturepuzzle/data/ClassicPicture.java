package com.khgame.picturepuzzle.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class ClassicPicture {
    @PrimaryKey
    @NonNull
    private String id;
    private String easy;
    private String medium;
    private String hard;

    public String getId() {
        return id;
    }

    public String getEasy() {
        return easy;
    }

    public String getMedium() {
        return medium;
    }

    public String getHard() {
        return hard;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEasy(String easy) {
        this.easy = easy;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public void setHard(String hard) {
        this.hard = hard;
    }
}
