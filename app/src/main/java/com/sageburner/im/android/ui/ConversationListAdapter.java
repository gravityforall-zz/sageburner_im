
package com.sageburner.im.android.ui;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.sageburner.im.android.BootstrapApplication;
import com.sageburner.im.android.R;
import com.sageburner.im.android.core.ConversationMessageItem;
import com.sageburner.im.android.core.ConversationViewHolder;
import com.sageburner.im.android.core.User;
import com.sageburner.im.android.util.CryptoUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter to
 */
public class ConversationListAdapter extends SingleTypeAdapter<ConversationMessageItem> {

    private final LayoutInflater inflater;

    /**
     * @param inflater
     * @param items
     */
    public ConversationListAdapter(final LayoutInflater inflater, final List<ConversationMessageItem> items) {
        super(inflater, R.layout.message_list_item);

        setItems(items);
        this.inflater = inflater;
    }

    @Override
    public long getItemId(final int position) {
        final String id = getItem(position).getObjectId();
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.iv_avatar, R.id.tv_name};
    }

    @Override
    protected void update(final int position, final ConversationMessageItem conversationMessageItem) {

        if (conversationMessageItem.isIncoming()) {
            updater.view.setBackgroundColor(Color.LTGRAY);
        } else {
            updater.view.setBackgroundColor(Color.WHITE);
        }

        Picasso.with(BootstrapApplication.getInstance())
                .load(conversationMessageItem.getAvatarUrl())
                .placeholder(R.drawable.gravatar_icon)
                .into(imageView(0));

        setText(1, String.format("%s",conversationMessageItem.getMessage().toString()));
    }

//    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ConversationMessageItem msgItem = getItem(position);

        Log.d("ConversationListAdapter::getView: ", msgItem.toString());

        boolean isIncoming = msgItem.isIncoming();
        String username;

        int layoutId;
        if (isIncoming) {
            layoutId = R.layout.conversation_list_item_in;
            username = BootstrapApplication.getInstance().getLocalUser().getUsername();
        } else {
            layoutId = R.layout.conversation_list_item_out;
            username = msgItem.getToUser().getUsername();
        }

        Log.i("ConversationListAdapter::getView: ", "layoutId: " + layoutId);

        convertView = inflater.inflate(layoutId, parent, false);

        Picasso.with(BootstrapApplication.getInstance())
                .load("dummy_avatar_url")
                .placeholder(R.drawable.gravatar_icon)
                .into((ImageView)convertView.findViewById(R.id.iv_avatar));

        String msgText = null;
        try {
            String message = msgItem.getMessage().toString();
            msgText = CryptoUtils.readCryptoMessage(message, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((TextView)convertView.findViewById(R.id.tv_message)).setText(msgText);

        return convertView;
    }
}