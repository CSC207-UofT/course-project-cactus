package com.cactus.ui;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        AppModule.class
})
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);

    void inject(SignupActivity signupActivity);

    void inject(DisplayingListsActivity displayingListsActivity);

    void inject(DisplayingItemsActivity displayingItemsActivity);

    void inject(CustomAdapter customAdapter);

    void inject(ListOptionsActivity listOptionsActivity);

    void inject(DisplayingTemplatesActivity displayingTemplatesActivity);
}
