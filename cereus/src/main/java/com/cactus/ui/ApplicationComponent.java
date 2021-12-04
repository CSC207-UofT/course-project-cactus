package com.cactus.ui;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        AppModule.class
})
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);

    void inject(AbstractActivity abstractActivity);

    void inject(SignupActivity signupActivity);

    void inject(DisplayingListsActivity displayingListsActivity);

    void inject(DisplayingItemsActivity displayingItemsActivity);

    void inject(CreateListActivity createListActivity);

    void inject(CreateTemplateActivity createTemplateActivity);

    void inject(CustomAdapter customAdapter);

    void inject (UserProfileActivity userProfileActivity);

    void inject (AddFriendActivity addFriendActivity);
}
