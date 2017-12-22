package com.khgame.picturepuzzle.di;

import android.app.Application;
import android.content.Context;

import com.khgame.picturepuzzle.common.Constant;
import com.khgame.picturepuzzle.common.SettingManager;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module @Singleton
public abstract class AppModule {

    @Binds
    abstract Context bindContext(Application application);

    @Provides
    public SettingManager provideSettingManager(Context context) {
        SettingManager.Initialize(context);
        return SettingManager.Instance();
    }

    @Provides
    public DisorderUtil provideDisorderUtil(SettingManager settingManager) {
        String easyTemplate = settingManager.getString(Constant.EASY_TEMPLATE_KEY, Constant.EASY_TEMPLATE_DEFAULT);
        String mediumTemplate = settingManager.getString(Constant.MEDIUM_TEMPLATE_KEY, Constant.MEDIUM_TEMPLATE_DEFAULT);
        String hardTemplate = settingManager.getString(Constant.HARD_TEMPLATE_KEY, Constant.HARD_TEMPLATE_DEFAULT);
        DisorderUtil disorderUtil = new DisorderUtil(easyTemplate, mediumTemplate, hardTemplate);
        disorderUtil.observeTemplate()
                .subscribe(template -> {
                    switch (GameLevel.getLevel(template)) {
                        case GameLevel.EASY: settingManager.setString(Constant.EASY_TEMPLATE_KEY, template); break;
                        case GameLevel.MEDIUM: settingManager.setString(Constant.MEDIUM_TEMPLATE_KEY, template); break;
                        case GameLevel.HARD: settingManager.setString(Constant.HARD_TEMPLATE_KEY, template); break;
                    }
                });
        return disorderUtil;
    }
}
