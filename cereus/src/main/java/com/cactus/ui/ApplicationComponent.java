package com.cactus.ui;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        AppModule.class
})
public interface ApplicationComponent {

    void inject(AbstractActivity abstractActivity);

    void inject(CustomAdapter customAdapter);
}
