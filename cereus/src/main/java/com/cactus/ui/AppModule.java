package com.cactus.ui;

import com.cactus.adapters.AuthAdapter;
import com.cactus.adapters.GroceryAdapter;
import com.cactus.adapters.WebAuthAdapter;
import com.cactus.adapters.WebGroceryAdapter;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public abstract class AppModule {

    @Provides
    static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Binds
    public abstract AuthAdapter bindAuthAdapter(WebAuthAdapter impl);

    @Binds
    public abstract GroceryAdapter bindGroceryAdapter(WebGroceryAdapter impl);
}
