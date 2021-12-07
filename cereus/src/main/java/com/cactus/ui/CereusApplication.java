package com.cactus.ui;

import android.app.Application;

/**
 * Class that defined the Cereus application
 */
public class CereusApplication extends Application {
    ApplicationComponent appComponent = DaggerApplicationComponent.create();
}
