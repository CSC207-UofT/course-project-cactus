package com.cactus.ui;

import dagger.Component;

@Component(modules = {
        AppModule.class
})
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
}
