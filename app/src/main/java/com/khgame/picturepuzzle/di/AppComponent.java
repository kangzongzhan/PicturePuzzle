package com.khgame.picturepuzzle.di;

import android.app.Application;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
}
