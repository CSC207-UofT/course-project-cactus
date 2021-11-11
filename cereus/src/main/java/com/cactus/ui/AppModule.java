package com.cactus.ui;

import com.cactus.adapters.AuthAdapter;
import com.cactus.adapters.GroceryAdapter;
import com.cactus.adapters.WebAuthAdapter;
import com.cactus.adapters.WebGroceryAdapter;
import com.cactus.systems.UserInteractFacade;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppModule {
//    @Provides
//    static TestClass provideTestClass() {
//        return new TestClass();
//    }

//    @Provides
//    static UserInteractFacade provideFacade() {
//        return new UserInteractFacade();
//    }

    @Binds
    public abstract AuthAdapter bindAuthAdapter(WebAuthAdapter impl);

    @Binds
    public abstract GroceryAdapter bindGroceryAdapter(WebGroceryAdapter impl);
}
