package com.khgame.picturepuzzle.classic;

import android.util.Log;

import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.db.operation.InsertClassicPictureOperation;
import com.khgame.picturepuzzle.model.ClassicPicture;
import com.khgame.picturepuzzle.operation.CopyAssetsToDiskOperation;
import com.khgame.picturepuzzle.operation.EmptyOperation;
import com.khgame.picturepuzzle.operation.Operation;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

/**
 * Created by Kisha Deng on 2/27/2017.
 */

public class ClassicManagerImpl implements ClassicManager {

    private EventBus bus = EventBus.getDefault();

    private static ClassicManagerImpl instance;
    private ClassicManagerImpl(){}
    public static ClassicManager getInstance() {
        synchronized (ClassicManagerImpl.class) {
            if(instance != null) {
                return instance;
            }
            instance = new ClassicManagerImpl();
        }
        return instance;
    }

    @Override
    public void initialize() {
        new EmptyOperation().callback(new Operation.Callback<Void, Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                for (int i = 1; i < 10; i++) {
                    String assets = "default0" + i + ".jpg";
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
}
