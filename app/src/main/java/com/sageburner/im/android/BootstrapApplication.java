

package com.sageburner.im.android;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import com.sageburner.im.android.core.User;
import com.sageburner.im.android.ibe.IBE;

/**
 * sageburner_im application
 */
public class BootstrapApplication extends Application {

    private static BootstrapApplication instance;

    private static User localUser;

    /**
     * Create main application
     */
    public BootstrapApplication() {
    }

    /**
     * Create main application
     *
     * @param context
     */
    public BootstrapApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // Perform injection
        Injector.init(getRootModule(), this);

    }

    private Object getRootModule() {
        return new RootModule();
    }

    /**
     * Create main application
     *
     * @param instrumentation
     */
    public BootstrapApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    public static BootstrapApplication getInstance() {
        return instance;
    }

    public static void setLocalUser(User localUser) {
        BootstrapApplication.localUser = localUser;
    }

    public static User getLocalUser() {
        return localUser;
    }
}
