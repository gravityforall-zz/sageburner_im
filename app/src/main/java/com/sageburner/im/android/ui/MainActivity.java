

package com.sageburner.im.android.ui;

import android.accounts.OperationCanceledException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import butterknife.Views;
import com.github.kevinsawicki.wishlist.Toaster;
import com.sageburner.im.android.BootstrapApplication;
import com.sageburner.im.android.BootstrapServiceProvider;
import com.sageburner.im.android.R;
import com.sageburner.im.android.authenticator.LogoutService;
import com.sageburner.im.android.core.User;
import com.sageburner.im.android.service.XMPPService;
import com.sageburner.im.android.core.BootstrapService;
import com.sageburner.im.android.events.NavItemSelectedEvent;
import com.sageburner.im.android.util.Ln;
import com.sageburner.im.android.util.SafeAsyncTask;
import com.sageburner.im.android.util.UIUtils;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;


/**
 * Initial activity for the application.
 *
 * If you need to remove the authentication from the application please see
 * {@link com.sageburner.im.android.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class MainActivity extends BootstrapFragmentActivity {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;
    @Inject protected XMPPService xmppService;

    private boolean userHasAuthenticated = false;
    //private boolean userHasAuthenticated = true;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        if(isTablet()) {
            setContentView(R.layout.main_activity_tablet);
        } else {
            setContentView(R.layout.main_activity);
        }

        // View injection with Butterknife
        Views.inject(this);

        // Set up navigation drawer
        title = drawerTitle = getTitle();

        if(!isTablet()) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerToggle = new ActionBarDrawerToggle(
                    this,                    /* Host activity */
                    drawerLayout,           /* DrawerLayout object */
                    R.drawable.ic_drawer,    /* nav drawer icon to replace 'Up' caret */
                    R.string.navigation_drawer_open,    /* "open drawer" description */
                    R.string.navigation_drawer_close) { /* "close drawer" description */

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    getSupportActionBar().setTitle(title);
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(drawerTitle);
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            // Set the drawer toggle as the DrawerListener
            drawerLayout.setDrawerListener(drawerToggle);

            navigationDrawerFragment = (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

            // Set up the drawer.
            navigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        checkAuth();
    }

    private boolean isTablet() {
        return UIUtils.isTablet(this);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(!isTablet()) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            drawerToggle.syncState();
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!isTablet()) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }


    private void initScreen() {
        if (userHasAuthenticated) {
            Ln.d("Foo");
            final FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new CarouselFragment())
                    .commit();
        }

    }

    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final BootstrapService svc = serviceProvider.getService(MainActivity.this);
                return svc != null;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
                    finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
                super.onSuccess(hasAuthenticated);
                userHasAuthenticated = true;

                //get localUser from application context
                User user = BootstrapApplication.getInstance().getLocalUser();
                //Connect to XMPP server
                Log.d("MainActivity::checkAuth::onSuccess: ", "Connecting as " + user.getUsername());
                connect(user);
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                //menuDrawer.toggleMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onNavigationItemSelected(NavItemSelectedEvent event) {

        Ln.d("Selected: %1$s", event.getItemPosition());

        switch(event.getItemPosition()) {
            case 0:
//                logout();
                Toaster.showShort(this, "logout pressed");
                break;
        }
    }

//    private void logout() {
//        logoutService.logout(new Runnable() {
//            @Override
//            public void run() {
//                // Calling a refresh will force the service to look for a logged in user
//                // and when it finds none the user will be requested to log in again.
//                //forceRefresh();
//                setContentView(R.layout.login_activity);
//                //View view = findViewById(R.id.navigation_drawer);
//                //view.invalidate();
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            disconnect();
            logout();
        } catch (Exception e) {

        }
    }

    protected LogoutService getLogoutService() {
        return logoutService;
    }

    private void logout() {
        getLogoutService().logout(new Runnable() {
            @Override
            public void run() {
                // Calling a refresh will force the service to look for a logged in user
                // and when it finds none the user will be requested to log in again.
                //forceRefresh();
            }
        });
    }

    //XMPP Stuff
    //    @Override
    //TODO: make this part of ItemListFragment??
    protected XMPPService getXMPPService() {
        return xmppService;
    }

    private void connect(User user) {
        getXMPPService().connect(user, new Runnable() {
            @Override
            public void run() {
                initScreen();
            }
        });
    }

    private void disconnect() {
        getXMPPService().disconnect(new Runnable() {
            @Override
            public void run() {
                //do something?
            }
        });
    }
}
