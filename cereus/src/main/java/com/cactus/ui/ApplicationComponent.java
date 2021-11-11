package com.cactus.ui;

import dagger.Component;

@Component(modules = {
        AppModule.class
})
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void inject(SignupActivity signupActivity);
    void inject(CreatingGroceryListActivity creatingGroceryListActivity);
    void inject(OptionsActivity optionsActivity);
}
