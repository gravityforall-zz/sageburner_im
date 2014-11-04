package com.sageburner.im.android.authenticator;

import android.util.Log;
import com.sageburner.im.android.core.Constants;
import com.sageburner.im.android.core.ConversationMessageItem;
import com.sageburner.im.android.util.Ln;
import com.sageburner.im.android.util.SafeAsyncTask;
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

    public void connect(final Runnable onSuccess) {
        new ConnectTask(onSuccess).execute();
    }

    public void disconnect(final Runnable onSuccess) {
        new DisconnectTask(onSuccess).execute();
    }

    public void sendMessage(ConversationMessageItem convMsgItem, final Runnable onSuccess) {
        new SendMessageTask(convMsgItem, onSuccess).execute();
    }

    private class ConnectTask extends SafeAsyncTask<Boolean> {

        private final Runnable onSuccess;

        protected ConnectTask(final Runnable onSuccess) {
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
                Log.i("XMPPService", "[SettingsDialog] Connected to " + xmppConn.getHost());
            } catch (Exception ex) {
                Log.e("XMPPService",  "[SettingsDialog] Failed to connect to "+ xmppConn.getHost());
                Log.e("XMPPService", ex.toString());
                xmppConn = null;
            }

            try {
                //log in
                xmppConn.login(Constants.XMPP.USERNAME, Constants.XMPP.PASSWORD);
                Log.i("XMPPService",  "Logged in as" + xmppConn.getUser());

                // Set the status to available
                Presence presence = new Presence(Presence.Type.available);
                xmppConn.sendPacket(presence);

                //get roster
                Roster roster = xmppConn.getRoster();

                Collection<RosterEntry> entries = roster.getEntries();
                for (RosterEntry entry : entries) {

                    Log.d("XMPPService",  "--------------------------------------");
                    Log.d("XMPPService", "RosterEntry " + entry);
                    Log.d("XMPPService", "User: " + entry.getUser());
                    Log.d("XMPPService", "Name: " + entry.getName());
                    Log.d("XMPPService", "Status: " + entry.getStatus());
                    Log.d("XMPPService", "Type: " + entry.getType());
                    Presence entryPresence = roster.getPresence(entry.getUser());

                    Log.d("XMPPService", "Presence Status: "+ entryPresence.getStatus());
                    Log.d("XMPPService", "Presence Type: " + entryPresence.getType());

                    Presence.Type type = entryPresence.getType();
                    if (type == Presence.Type.available)
                        Log.d("XMPPService", "Presence AVIALABLE");
                        Log.d("XMPPService", "Presence : " + entryPresence);
                }
            } catch (Exception ex) {
                Log.e("XMPPService", "Failed to log in as "+  Constants.XMPP.USERNAME);
                Log.e("XMPPService", ex.toString());
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
            message.setBody(convMsgItem.getMessageText());
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
}