package com.khgame.picturepuzzle.classic;

import android.content.Context;

import com.google.android.gms.games.Game;
import com.khgame.picturepuzzle.common.Result;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.data.source.local.AppDatabase;
import com.khgame.picturepuzzle.db.model.ClassicPicturePo;
import com.khgame.picturepuzzle.db.operation.GetClassicPictureByUuidOperation;
import com.khgame.picturepuzzle.db.operation.InsertClassicPictureOperation;
import com.khgame.picturepuzzle.db.operation.QueryAllClassicPicturesOperation;
import com.khgame.picturepuzzle.db.operation.UpdateClassicPictureOperation;
import com.khgame.picturepuzzle.events.ClassicPictureLoadEvent;
import com.khgame.picturepuzzle.events.ClassicPicturesLoadEvent;
import com.khgame.picturepuzzle.model.ClassicPicture;
import com.khgame.picturepuzzle.operation.CopyAssetsToDiskOperation;
import com.khgame.picturepuzzle.operation.EmptyOperation;
import com.khgame.picturepuzzle.operation.Operation;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Kisha Deng on 2/27/2017.
 */

public class ClassicPictureManagerImpl implements ClassicPictureManager {

    private EventBus bus = EventBus.getDefault();

    private static ClassicPictureManagerImpl instance;
    private ClassicPictureManagerImpl(){}
    public static ClassicPictureManager getInstance() {
        synchronized (ClassicPictureManagerImpl.class) {
            if (instance != null) {
                return instance;
            }
            instance = new ClassicPictureManagerImpl();
        }
        return instance;
    }

    @Override
    public void initialize(Context context) {
        new EmptyOperation().callback(new Operation.Callback<Void, Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                for (int i = 1; i < 10; i++) {
                    String assets = "default0" + i + ".jpg";

                    com.khgame.picturepuzzle.data.ClassicPicture cp = new com.khgame.picturepuzzle.data.ClassicPicture();
                    cp.setId(UUID.randomUUID().toString());
                    cp.setEasy(DisorderUtil.newDisorderString(GameLevel.EASY));
                    cp.setMedium(DisorderUtil.newDisorderString(GameLevel.MEDIUM));
                    cp.setHard(DisorderUtil.newDisorderString(GameLevel.HARD));
                    AppDatabase.getInstance(context).classicDao().insertTask(cp);


                    new CopyAssetsToDiskOperation(assets, UUID.randomUUID().toString()).callback(new Operation.Callback<ClassicPicture, Void>() {
                        @Override
                        public void onSuccess(final ClassicPicture classicPicture) {
                            classicPicture.easyData = DisorderUtil.newDisorderString(GameLevel.EASY);
                            classicPicture.mediumData = DisorderUtil.newDisorderString(GameLevel.MEDIUM);
                            classicPicture.hardData = DisorderUtil.newDisorderString(GameLevel.HARD);
                            new InsertClassicPictureOperation(classicPicture).callback(new Operation.Callback<ClassicPicture, Void>() {
                                @Override
                                public void onSuccess(ClassicPicture picture) {
                                }
                                @Override
                                public void onFailure(Void aVoid) {
                                }
                            }).execute();
                        }
                    }).execute();
                }
                bus.post(new InitClassicPictureFinishEvent());
            }
        }).enqueue();
    }

    @Override
    public void getAllClassicPictures() {
        new QueryAllClassicPicturesOperation().callback(new Operation.Callback<List<ClassicPicturePo>, Void>() {
            @Override
            public void onSuccess(List<ClassicPicturePo> classicPicturePos) {
                List<ClassicPicture> classicPictures = new ArrayList<>();
                for (ClassicPicturePo classicPicturePo : classicPicturePos) {
                    ClassicPicture classicPicture = new ClassicPicture();
                    classicPicture.uuid = classicPicturePo.uuid;
                    classicPicture.easyData = classicPicturePo.easyData;
                    classicPicture.mediumData = classicPicturePo.mediumData;
                    classicPicture.hardData = classicPicturePo.hardData;
                    classicPictures.add(classicPicture);
                }

                ClassicPicturesLoadEvent event = new ClassicPicturesLoadEvent(Result.Success);
                event.classicPictures = classicPictures;
                bus.post(event);
            }
        }).enqueue();
    }

    @Override
    public void getClassicPictureByUuid(String uuid) {
        new GetClassicPictureByUuidOperation(uuid).callback(new Operation.Callback<ClassicPicturePo, Void>() {
            @Override
            public void onSuccess(ClassicPicturePo classicPicturePo) {
                ClassicPicture classicPicture = new ClassicPicture();
                classicPicture.uuid = classicPicturePo.uuid;
                classicPicture.easyData = classicPicturePo.easyData;
                classicPicture.mediumData = classicPicturePo.mediumData;
                classicPicture.hardData = classicPicturePo.hardData;

                ClassicPictureLoadEvent event = new ClassicPictureLoadEvent(Result.Success);
                event.classicPicture = classicPicture;
                bus.post(event);
            }
        }).enqueue();
    }

    @Override
    public void updateClassicPicture(ClassicPicture classicPicture) {
        new UpdateClassicPictureOperation(classicPicture).enqueue();
    }
}
