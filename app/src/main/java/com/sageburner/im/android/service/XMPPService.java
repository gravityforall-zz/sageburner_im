package com.sageburner.im.android.service;

import android.content.Context;
import android.util.Log;
import com.sageburner.im.android.BootstrapApplication;
import com.sageburner.im.android.core.Constants;
import com.sageburner.im.android.core.ConversationMessageItem;
import com.sageburner.im.android.core.User;
import com.sageburner.im.android.util.Ln;
import com.sageburner.im.android.util.SafeAsyncTask;
import com.sageburner.im.android.util.UserUtils;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.Collection;


/**
 * Class used for accessing the XMPP service.
 */
public class XMPPService {

    private XMPPConnection xmppConn;

    public XMPPService() { }

    public void setPacketListener(PacketListener packetListener, PacketFilter packetFilter) {
        if (xmppConn != null) {
            xmppConn.addPacketListener(packetListener, packetFilter);
        } else {
            Ln.e("XMPPService: setPacketListener failed", "XMPPConnection was null");
        }
    }

    public Roster getRoster() {
        return xmppConn.getRoster();
    }

    public void connect(User user, final Runnable onSuccess) {
        new ConnectTask(user, onSuccess).execute();
    }

    public void disconnect(final Runnable onSuccess) {
        new DisconnectTask(onSuccess).execute();
    }

    public void sendMessage(ConversationMessageItem convMsgItem, final Runnable onSuccess) {
        new SendMessageTask(convMsgItem, onSuccess).execute();
    }

    public void addFriend(XMPPConnection xmppConn, String userName, final Runnable onSuccess) {
        new AddFriendTask(xmppConn, userName, onSuccess).execute();
    }

    private class ConnectTask extends SafeAsyncTask<Boolean> {

        private final User user;
        private final Runnable onSuccess;

        protected ConnectTask(User user, final Runnable onSuccess) {
            this.user = user;
            this.onSuccess = onSuccess;
        }

        @Override
        public Boolean call() throws Exception {
            // Create a connection
            ConnectionConfiguration connConfig = new ConnectionConfiguration(Constants.XMPP.HOST, Constants.XMPP.PORT);
            connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            xmppConn = new XMPPTCPConnection(connConfig);

            try {
                xmppConn.connect();
                Log.i("XMPPService::call: ", "[SettingsDialog] Connected to " + xmppConn.getHost());
            } catch (Exception ex) {
                Log.e("XMPPService::call: ",  "[SettingsDialog] Failed to connect to "+ xmppConn.getHost());
                Log.e("XMPPService::call: ", ex.toString());
                xmppConn = null;
            }

            try {
                //get localUser from application context
                //user = ((BootstrapApplication) BootstrapApplication.getInstance()).getLocalUser();
                Log.d("XMPPService::call: ", " username: " + user.getUsername());
                //log in
                String userAlias = UserUtils.parseUserAlias(user);
                String password = user.getXmppPassword();
                Log.d("XMPPService::call: ", " userAlias: " + userAlias);
                xmppConn.login(userAlias, password);
                Log.i("XMPPService::call: ",  "Logged in as" + xmppConn.getUser());

                // Set the status to available
                Presence presence = new Presence(Presence.Type.available);
                xmppConn.sendPacket(presence);

                //get roster
                Roster roster = xmppConn.getRoster();

                Collection<RosterEntry> entries = roster.getEntries();
                for (RosterEntry entry : entries) {

                    Log.d("XMPPService::call: ",  "--------------------------------------");
                    Log.d("XMPPService::call: ", "RosterEntry " + entry);
                    Log.d("XMPPService::call: ", "User: " + entry.getUser());
                    Log.d("XMPPService::call: ", "Name: " + entry.getName());
                    Log.d("XMPPService::call: ", "Status: " + entry.getStatus());
                    Log.d("XMPPService::call: ", "Type: " + entry.getType());
                    Presence entryPresence = roster.getPresence(entry.getUser());

                    Log.d("XMPPService::call: ", "Presence Status: "+ entryPresence.getStatus());
                    Log.d("XMPPService::call: ", "Presence Type: " + entryPresence.getType());

                    Presence.Type type = entryPresence.getType();
                    if (type == Presence.Type.available)
                        Log.d("XMPPService::call: ", "Presence AVIALABLE");
                        Log.d("XMPPService::call: ", "Presence : " + entryPresence);
                }
            } catch (Exception ex) {
                Log.e("XMPPService::call: ", "Failed to log in as "+  user.getUsername());
                Log.e("XMPPService::call:", ex.toString());
                xmppConn = null;
            }

            //TODO: Fix this return statement
            return true;
        }

        @Override
        protected void onSuccess(final Boolean connectionSuccess) throws Exception {
            //TODO: implement onSuccess method
            super.onSuccess(connectionSuccess);

            Ln.d("Connection successful: %s", connectionSuccess);
            onSuccess.run();
        }

        @Override
        protected void onException(final Exception e) throws RuntimeException {
            //TODO: implement onException method
            super.onException(e);

            Ln.e(e.getCause(), "Connection failed!!!");
        }
    }

    private class DisconnectTask extends SafeAsyncTask<Boolean> {

        private final Runnable onSuccess;

        protected DisconnectTask(final Runnable onSuccess) {
            this.onSuccess = onSuccess;
        }

        @Override
        public Boolean call() throws Exception {
            //TODO: clean up call method
            try {
                xmppConn.disconnect();
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onSuccess(final Boolean disconnectionSuccess) throws Exception {
            //TODO: implement onSuccess method
            super.onSuccess(disconnectionSuccess);

            Ln.d("Disconnection successful: %s", disconnectionSuccess);
            onSuccess.run();
        }

        @Override
        protected void onException(final Exception e) throws RuntimeException {
            //TODO: implement onException method
            super.onException(e);

            Ln.e(e.getCause(), "Disconnection failed.");
        }
    }

    private class SendMessageTask extends SafeAsyncTask<Boolean> {

        private final ConversationMessageItem convMsgItem;
        private final Runnable onSuccess;

        protected SendMessageTask(ConversationMessageItem convMsgItem, final Runnable onSuccess) {
            this.convMsgItem = convMsgItem;
            this.onSuccess = onSuccess;
        }

        @Override
        public Boolean call() throws Exception {
            Message message = new Message(convMsgItem.getToUser().getUsername(), Message.Type.chat);
            message.setBody(convMsgItem.getMessage().toString());
            xmppConn.sendPacket(message);
            return true;
        }

        @Override
        protected void onSuccess(final Boolean messageSendSuccess) throws Exception {
            //TODO: implement onSuccess method
            super.onSuccess(messageSendSuccess);

            Ln.d("Message send successful: %s", messageSendSuccess);
            onSuccess.run();
        }

        @Override
        protected void onException(final Exception e) throws RuntimeException {
            //TODO: implement onException method
            super.onException(e);

            Ln.e(e.getCause(), "Message send failed.");
        }
    }

    private class AddFriendTask extends SafeAsyncTask<Boolean> {

        private final XMPPConnection xmppConn;
        private final String userName;
        private final Runnable onSuccess;

        protected AddFriendTask(XMPPConnection xmppConn, String userName, final Runnable onSuccess) {
            this.xmppConn = xmppConn;
            this.userName = userName;
            this.onSuccess = onSuccess;
        }

        @Override
        public Boolean call() throws Exception {
            //public void createEntry(String user, String name, String[] groups) throws XMPPException

            //user - the user. (e.g. johndoe@jabber.org)
            //name - the nickname of the user.
            //groups - the list of group names the entry will belong to, or null if the the roster entry won't belong to a group.

            Roster roster = xmppConn.getRoster();
            roster.createEntry(userName, null, null);
            return true;
        }

        @Override
        protected void onSuccess(final Boolean addFriendSuccess) throws Exception {
            //TODO: implement onSuccess method
            super.onSuccess(addFriendSuccess);

            Ln.d("Add friend successful: %s", addFriendSuccess);
            onSuccess.run();
        }

        @Override
        protected void onException(final Exception e) throws RuntimeException {
            //TODO: implement onException method
            super.onException(e);

            Ln.e(e.getCause(), "Add friend failed.");
        }
    }
}