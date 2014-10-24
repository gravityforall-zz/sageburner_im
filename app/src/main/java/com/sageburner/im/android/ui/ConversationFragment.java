package com.sageburner.im.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ConversationFragment extends ItemListFragment<ConversationMessageItem> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;
    @Inject protected XMPPService xmppService;

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }
    //    @Override
    //TODO: make this part of ItemListFragment??
    protected XMPPService getXMPPService() {
        return xmppService;
    }

    //XMPP Stuff
    private String recipient = "ryan@sageburner.com";
    private EditText msgInput;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(R.string.no_messages);
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //XMPP Send Button Listener
        Button send = (Button) this.getActivity().findViewById(R.id.b_send);
        msgInput = (EditText) this.getActivity().findViewById(R.id.et_input_msg);

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //String to = recipient;
                String messageText = msgInput.getText().toString();
                Log.i("XMPPChatDemoActivity ", "Sending text " + messageText + " to " + recipient);

                ConversationMessageItem convMsgItem = new ConversationMessageItem();
                convMsgItem.setMessageText(messageText);

                try {
                    sendMessage(convMsgItem);
                    items.add(convMsgItem);
                } catch (Exception e) {
                    Log.e("XMPPChatDemoActivity ", "Sending text to " + recipient + " failed!");
//                    Log.e("XMPPChatDemoActivity ", e.getMessage());
                }
            }
        });

        //XMPP Incoming Packet Listener
        PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
        xmppService.setPacketListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                if (message.getBody() != null) {
                    String fromName = StringUtils.parseBareAddress(message.getFrom());
                    Log.i("XMPPChatDemoActivity ", " Text Recieved " + message.getBody() + " from " + fromName);

                    ConversationMessageItem convMsgItem = new ConversationMessageItem();
                    convMsgItem.setMessageText(message.getBody());

                    items.add(convMsgItem);
                }
            }
        }, filter);
    }

    @Override
    public Loader<List<ConversationMessageItem>> onCreateLoader(final int id, final Bundle args) {
        return new ThrowableLoader<List<ConversationMessageItem>>(getActivity(), items) {
            @Override
            public List<ConversationMessageItem> loadData() throws Exception {
                List<ConversationMessageItem> latest = null;
//                latest = createDummyMessages();
                if (latest != null) {
                    return latest;
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    public void onLoadFinished(final Loader<List<ConversationMessageItem>> loader, final List<ConversationMessageItem> items) {
        super.onLoadFinished(loader, items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        //final User user = ((User) l.getItemAtPosition(position));
        //Object object = l.getItemAtPosition(position);
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_users;
    }

    @Override
    protected AlternatingColorListAdapter<ConversationMessageItem> createAdapter(final List<ConversationMessageItem> items) {
        return new ConversationListAdapter(getActivity().getLayoutInflater(), items);
    }

    private void sendMessage(ConversationMessageItem convMsgItem) {
        getXMPPService().sendMessage(convMsgItem, new Runnable() {
            @Override
            public void run() {
                // Calling a refresh will force the service to...?
                forceRefresh();
            }
        });
    }

    private List<ConversationMessageItem> createDummyMessages() {
        List<ConversationMessageItem> msgList = new ArrayList<ConversationMessageItem>();

        User alice = new User();
        alice.setFirstName("Alice");
        alice.setUsername("alice@sageburner.com");
        User bob = new User();
        bob.setFirstName("Bob");
        bob.setUsername("bob@sageburner.com");

        ConversationMessageItem msgItem = new ConversationMessageItem();
        msgItem.setFromUser(alice);
        msgItem.setToUser(bob);
        msgItem.setMessageText("Hello!");
        msgList.add(msgItem);

        msgItem = new ConversationMessageItem();
        msgItem.setFromUser(bob);
        msgItem.setToUser(alice);
        msgItem.setMessageText("Hi!");
        msgList.add(msgItem);

        msgItem = new ConversationMessageItem();
        msgItem.setFromUser(bob);
        msgItem.setToUser(alice);
        msgItem.setMessageText("How are you?");
        msgList.add(msgItem);

        msgItem = new ConversationMessageItem();
        msgItem.setFromUser(alice);
        msgItem.setToUser(bob);
        msgItem.setMessageText("Fine, thanks.");
        msgList.add(msgItem);

        return msgList;
    }
}