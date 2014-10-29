package com.sageburner.im.android.ui;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.InjectView;
import com.sageburner.im.android.BootstrapServiceProvider;
import com.sageburner.im.android.Injector;
import com.sageburner.im.android.R;
import com.sageburner.im.android.authenticator.LogoutService;
import com.sageburner.im.android.authenticator.XMPPService;
import com.sageburner.im.android.core.ConversationMessageItem;
import com.sageburner.im.android.core.User;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import javax.inject.Inject;
import java.util.*;

import static com.sageburner.im.android.core.Constants.Extra.USER;

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

                //old code below.
                //I left this here to see how they were utilizing serviceProvider
/*                try {
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
                }*/
            }
        };
    }

    @Override
    public void onLoadFinished(final Loader<List<User>> loader, final List<User> items) {
        super.onLoadFinished(loader, items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        //final User user = ((User) l.getItemAtPosition(position));

        ViewPager pager = (ViewPager) getActivity().findViewById(R.id.vp_pages);
        pager.setCurrentItem(1, true);
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
            user.setUsername(entry.getUser());

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