package com.cactus.ui;

import com.cactus.adapters.AuthAdapter;
import com.cactus.adapters.GroceryAdapter;
import com.cactus.adapters.WebAuthAdapter;
import com.cactus.adapters.WebGroceryAdapter;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class AppModule {

    @Binds
    public abstract AuthAdapter bindAuthAdapter(WebAuthAdapter impl);

    @Binds
    public abstract GroceryAdapter bindGroceryAdapter(WebGroceryAdapter impl);
}
