package com.khgame.picturepuzzle.db.model;

import android.os.Bundle;

/**
 * Created by zkang on 2017/2/18.
 */

public class ClassicPicture {

    public long _id;
    public String assetsPath;
    public String localPath;
    public String networkPath;
    public String easyData;
    public String mediumData;
    public String hardData;

    public Bundle toBundle(){

        Bundle bundle = new Bundle();
        bundle.putLong("_id", _id);
        bundle.putString("assetsPath", assetsPath);
        bundle.putString("localPath", localPath);
        bundle.putString("networkPath", networkPath);
        bundle.putString("easyData", easyData);
        bundle.putString("mediumData", mediumData);
        bundle.putString("hardData", hardData);
        return bundle;
    }

    public static ClassicPicture fromBundle(Bundle bundle) {
        ClassicPicture picture = new ClassicPicture();
        picture._id = bundle.getLong("_id");
        picture.assetsPath = bundle.getString("assetsPath");
        picture.localPath = bundle.getString("localPath");
        picture.networkPath = bundle.getString("networkPath");
        picture.easyData = bundle.getString("easyData");
        picture.mediumData = bundle.getString("mediumData");
        picture.hardData = bundle.getString("hardData");
        return picture;
    }

}
