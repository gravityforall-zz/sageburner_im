package com.sageburner.im.android.ui;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;
import butterknife.InjectView;
import com.sageburner.im.android.BootstrapServiceProvider;
import com.sageburner.im.android.Injector;
import com.sageburner.im.android.R;
import com.sageburner.im.android.authenticator.LogoutService;
import com.sageburner.im.android.core.User;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.sageburner.im.android.core.Constants.Extra.USER;

public class FriendsListFragment extends ItemListFragment<User> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.no_users);
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);
    }

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }

    @Override
    public Loader<List<User>> onCreateLoader(final int id, final Bundle args) {
        final List<User> initialItems = items;
        return new ThrowableLoader<List<User>>(getActivity(), items) {
            @Override
            public List<User> loadData() throws Exception {

                try {
                    List<User> latest = null;

                    if (getActivity() != null) {
                        latest = serviceProvider.getService(getActivity()).getUsers();
                    }

                    if (latest != null) {
                        return latest;
                    } else {
                        return Collections.emptyList();
                    }
                } catch (final OperationCanceledException e) {
                    final Activity activity = getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                    return initialItems;
                }
            }
        };
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        //final User user = ((User) l.getItemAtPosition(position));

        ViewPager pager = (ViewPager) getActivity().findViewById(R.id.vp_pages);
        pager.setCurrentItem(1, true);
    }

    @Override
    public void onLoadFinished(final Loader<List<User>> loader, final List<User> items) {
        super.onLoadFinished(loader, items);
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_users;
    }

    @Override
    protected AlternatingColorListAdapter<User> createAdapter(final List<User> items) {
        return new FriendsListAdapter(getActivity().getLayoutInflater(), items);
    }
}