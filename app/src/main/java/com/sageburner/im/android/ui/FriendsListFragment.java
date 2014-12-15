package com.sageburner.im.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;
import com.sageburner.im.android.BootstrapServiceProvider;
import com.sageburner.im.android.Injector;
import com.sageburner.im.android.R;
import com.sageburner.im.android.authenticator.LogoutService;
import com.sageburner.im.android.service.XMPPService;
import com.sageburner.im.android.core.User;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FriendsListFragment extends ItemListFragment<User> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;
    @Inject protected XMPPService xmppService;

    private FriendsListAdapter friendsListAdapter;

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.no_friends);
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //XMPP Roster Listener
        Roster roster = xmppService.getRoster();

        roster.addRosterListener(new RosterListener() {
            public void entriesAdded(Collection<String> addresses) {}
            public void entriesDeleted(Collection<String> addresses) {}
            public void entriesUpdated(Collection<String> addresses) {}

            @Override
            public void presenceChanged(Presence presence) {
                //reset the current friends list
                items.clear();

                System.out.println("Presence changed: " + presence.getFrom() + " " + presence);

                List<User> userList = mapRosterToUserList();

                //add all roster entries to the friends list
                items.addAll(userList);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //update list adapter from UI thread
                        friendsListAdapter.setItems(items);
                    }
                });
            }
        });
    }

    @Override
    public Loader<List<User>> onCreateLoader(final int id, final Bundle args) {

        return new ThrowableLoader<List<User>>(getActivity(), items) {
            @Override
            public List<User> loadData() throws Exception {

                List<User> userList = mapRosterToUserList();

                if (userList.size() > 0) {
                    return userList;
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    public void onLoadFinished(final Loader<List<User>> loader, final List<User> items) {
        super.onLoadFinished(loader, items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final User user = ((User) l.getItemAtPosition(position));

        ViewPager pager = (ViewPager) getActivity().findViewById(R.id.vp_pages);
        BootstrapPagerAdapter bootstrapPagerAdapter = (BootstrapPagerAdapter) pager.getAdapter();

        ConversationFragment conversationFragment = new ConversationFragment();
        conversationFragment.setRecipient(user);

        //TODO allow app to persist previous fragment data (conversation list array) - but only for session
        if (bootstrapPagerAdapter.getCount() > 1 && bootstrapPagerAdapter.getItem(1) != null) {
//            ViewGroup container = ((ViewGroup)getView().getParent());
//            container.removeView(bootstrapPagerAdapter.getItem(1).getView());
            bootstrapPagerAdapter.removeFragment(pager, 1);
        }
        int fragmentIndex = bootstrapPagerAdapter.addFragment(conversationFragment, user.getAlias(), 1);
        bootstrapPagerAdapter.notifyDataSetChanged();

        pager.setCurrentItem(fragmentIndex, true);
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_users;
    }

    @Override
    protected AlternatingColorListAdapter<User> createAdapter(final List<User> items) {
        friendsListAdapter = new FriendsListAdapter(getActivity().getLayoutInflater(), items);
        return friendsListAdapter;
    }

    private List<User> mapRosterToUserList() {
        List<User> userList = new ArrayList<User>();
        User user;

        Roster roster = xmppService.getRoster();
        Collection<RosterEntry> entries = roster.getEntries();

        Presence entryPresence;

        for (RosterEntry entry : entries) {
            user = new User();

            String username = entry.getUser();
            user.setUsername(username);
            user.setAlias(username.split("@")[0]);

            entryPresence = roster.getPresence(entry.getUser());
            Presence.Type type = entryPresence.getType();
            if (type == Presence.Type.available) {
                user.setOnlineStatus(User.OnlineStatus.ONLINE);
            } else {
                user.setOnlineStatus(User.OnlineStatus.OFFLINE);
            }

            userList.add(user);
        }

        return userList;
    }

}