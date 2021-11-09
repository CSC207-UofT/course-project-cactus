package com.cactus.ui;

import android.app.Application;

public class CereusApplication extends Application {
    ApplicationComponent appComponent = DaggerApplicationComponent.create();
}
