package com.cactus.ui;

import com.cactus.systems.UserInteractFacade;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        AppModule.class
})
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void inject(SignupActivity signupActivity);
    void inject(CreatingGroceryListActivity creatingGroceryListActivity);
    void inject(OptionsActivity optionsActivity);

//    void inject(UserInteractFacade userInteractFacade);
}
