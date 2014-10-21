package com.sageburner.im.android.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Context;
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

import javax.inject.Inject;
import java.util.Collection;


/**
 * Class used for accessing the XMPP service.
 */
public class XMPPService {

    private XMPPConnection xmppConn;

    public XMPPConnection getXmppConn() {
        return xmppConn;
    }

    public void setXmppConn(XMPPConnection xmppConn) {
        this.xmppConn = xmppConn;
    }

//    @Inject
//    public XMPPService(final XMPPConnection xmppConn) {
//        this.xmppConn = xmppConn;
//    }

    public void setPacketListener(PacketListener packetListener, PacketFilter packetFilter) {
        if (xmppConn != null) {
            xmppConn.addPacketListener(packetListener, packetFilter);
        } else {
            Ln.e("XMPPService: setPacketListener failed", "XMPPConnection was null");
        }
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
            setXmppConn(new XMPPTCPConnection(connConfig));

            try {
                getXmppConn().connect();
                Log.i("XMPPChatDemoActivity", "[SettingsDialog] Connected to " + xmppConn.getHost());
            } catch (Exception ex) {
                Log.e("XMPPChatDemoActivity",  "[SettingsDialog] Failed to connect to "+ xmppConn.getHost());
                Log.e("XMPPChatDemoActivity", ex.toString());
                setXmppConn(null);
            }

            try {
                getXmppConn().login(Constants.XMPP.USERNAME, Constants.XMPP.PASSWORD);
                Log.i("XMPPChatDemoActivity",  "Logged in as" + xmppConn.getUser());

                // Set the status to available
                Presence presence = new Presence(Presence.Type.available);
                getXmppConn().sendPacket(presence);
                //setXmppConn(xmppConn);

                Roster roster = getXmppConn().getRoster();
                Collection<RosterEntry> entries = roster.getEntries();
                for (RosterEntry entry : entries) {

                    Log.d("XMPPChatDemoActivity",  "--------------------------------------");
                    Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
                    Log.d("XMPPChatDemoActivity", "User: " + entry.getUser());
                    Log.d("XMPPChatDemoActivity", "Name: " + entry.getName());
                    Log.d("XMPPChatDemoActivity", "Status: " + entry.getStatus());
                    Log.d("XMPPChatDemoActivity", "Type: " + entry.getType());
                    Presence entryPresence = roster.getPresence(entry.getUser());

                    Log.d("XMPPChatDemoActivity", "Presence Status: "+ entryPresence.getStatus());
                    Log.d("XMPPChatDemoActivity", "Presence Type: " + entryPresence.getType());

                    Presence.Type type = entryPresence.getType();
                    if (type == Presence.Type.available)
                        Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
                        Log.d("XMPPChatDemoActivity", "Presence : " + entryPresence);
                    }
            } catch (Exception ex) {
                Log.e("XMPPChatDemoActivity", "Failed to log in as "+  Constants.XMPP.USERNAME);
                Log.e("XMPPChatDemoActivity", ex.toString());
                setXmppConn(null);
            }

            //TODO: Fix this return statement
            return true;
        }

        @Override
        protected void onSuccess(final Boolean connectionSuccess) throws Exception {
            //TODO: implement onSuccess method

            Ln.d("Connection succeeded!");
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
        protected void onSuccess(final Boolean disconnectedSuccessfully) throws Exception {
            //TODO: implement onSuccess method
            super.onSuccess(disconnectedSuccessfully);

            Ln.d("Disconnection successful: %s", disconnectedSuccessfully);
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
            //TODO: implement call method

            //convMsgItem.getMessageText();
            //Message message = new Message(to, Message.Type.chat);
            //message.setBody(text);
            //connection.sendPacket(msg);

            if (5 > 10) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onSuccess(final Boolean messageSendSuccessful) throws Exception {
            //TODO: implement onSuccess method
            super.onSuccess(messageSendSuccessful);

            Ln.d("Message send successful: %s", messageSendSuccessful);
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