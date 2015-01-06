package com.sageburner.im.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.sageburner.im.android.BootstrapServiceProvider;
import com.sageburner.im.android.Injector;
import com.sageburner.im.android.R;
import com.sageburner.im.android.authenticator.LogoutService;
import com.sageburner.im.android.core.Constants;
import com.sageburner.im.android.service.XMPPService;
import com.sageburner.im.android.core.ConversationMessageItem;
import com.sageburner.im.android.core.User;
import com.sageburner.im.android.util.CryptoMessage;
import com.sageburner.im.android.util.CryptoUtils;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationFragment extends ItemListFragment<ConversationMessageItem> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;
    @Inject protected XMPPService xmppService;

    private ConversationListAdapter conversationListAdapter;

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }

    //XMPP Stuff
    private User recipient;
    private EditText msgInput;

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

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
                String messageText = msgInput.getText().toString();

                if (!TextUtils.isEmpty(messageText)) {
                    String username = recipient.getUsername();
                    Log.i("ConversationFragment ", "Sending text " + messageText + " to " + username);

                    ConversationMessageItem convMsgItem = new ConversationMessageItem();
                    convMsgItem.setToUser(recipient);

                    CryptoMessage cryptoMessage = null;
                    try {
                        cryptoMessage = CryptoUtils.createCryptoMessage(messageText, username);
                        Log.d("ConversationFragment::onViewCreated: ", " cryptoMessage.toString(): " + cryptoMessage.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    convMsgItem.setMessage(cryptoMessage);

                    convMsgItem.setIncoming(false);

                    try {
                        sendMessage(convMsgItem);
                        items.add(convMsgItem);
                        conversationListAdapter.setItems(items);
                    } catch (Exception e) {
                        Log.e("ConversationFragment::onViewCreated: ", "Sending text to " + recipient + " failed!");
                    }
                }

                msgInput.setText("");
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
                    Log.d("ConversationFragment::onViewCreated: ", " Text Recieved " + message.getBody() + " from " + fromName);

                    ConversationMessageItem convMsgItem = new ConversationMessageItem();

                    CryptoMessage cryptoMessage = null;
                    String messageBody = null;
                    try {
                        messageBody = message.getBody();
                        cryptoMessage = CryptoUtils.createCryptoMessage(messageBody);
                        Log.d("ConversationFragment::onViewCreated: ", " cryptoMessage: " + cryptoMessage.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    convMsgItem.setMessage(cryptoMessage);

                    convMsgItem.setIncoming(true);
                    items.add(convMsgItem);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //update list adapter from UI thread
                            conversationListAdapter.setItems(items);
                        }
                    });

                }
            }
        }, filter);
    }

    @Override
    public Loader<List<ConversationMessageItem>> onCreateLoader(final int id, final Bundle args) {
        return new ThrowableLoader<List<ConversationMessageItem>>(getActivity(), items) {
            @Override
            public List<ConversationMessageItem> loadData() throws Exception {
                return createNewConversationMessage();
//                return Collections.emptyList();
            }
        };
    }

    @Override
    public void onLoadFinished(final Loader<List<ConversationMessageItem>> loader, final List<ConversationMessageItem> items) {
        super.onLoadFinished(loader, items);
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_messages;
    }

    @Override
    protected SingleTypeAdapter<ConversationMessageItem> createAdapter(final List<ConversationMessageItem> items) {
        conversationListAdapter = new ConversationListAdapter(getActivity().getLayoutInflater(), items);
        return conversationListAdapter;
    }

    private void sendMessage(ConversationMessageItem convMsgItem) {
        xmppService.sendMessage(convMsgItem, new Runnable() {
            @Override
            public void run() {
                //code to be run onSuccess of message send
            }
        });
    }

    private List<ConversationMessageItem> createNewConversationMessage() throws Exception {
        List<ConversationMessageItem> msgList = new ArrayList<ConversationMessageItem>();

        User system = new User();
        system.setFirstName("System");
        system.setUsername("system@sageburner.com");
        User user1 = new User();
        user1.setFirstName("User");
        user1.setUsername("user1@sageburner.com");

        ConversationMessageItem msgItem = new ConversationMessageItem();
        msgItem.setFromUser(system);
        msgItem.setToUser(user1);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d h:mm");
        CryptoMessage cryptoMessage = CryptoUtils.createCryptoMessage(sdf.format(new Date()), user1.getUsername());
        msgItem.setMessage(cryptoMessage);
        msgList.add(msgItem);

        return msgList;
    }
}