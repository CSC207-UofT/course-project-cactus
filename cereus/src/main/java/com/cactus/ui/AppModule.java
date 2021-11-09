package com.cactus.ui;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Provides
    static TestClass provideTestClass() {
        return new TestClass();
    }
}
