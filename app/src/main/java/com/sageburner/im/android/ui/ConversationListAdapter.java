
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
        super(inflater, R.layout.user_list_item);

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

//        setText(1, String.format("%1$s %2$s", conversationMessageItem.getToUser().getFirstName(), conversationMessageItem.getToUser().getLastName()));
        setText(1, String.format("%s",conversationMessageItem.getMessageText()));
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ConversationMessageItem msgItem = getItem(position);
//        boolean isIncoming = msgItem.isIncoming();
//
//        int layoutId;
//        if (isIncoming) {
//            layoutId = R.layout.conversation_list_item_in;
//        } else {
//            layoutId = R.layout.conversation_list_item_out;
//        }
//
//        Log.i("XMPPChatDemoActivity ", "layoutId: " + layoutId);
//
//        //viewholder
//        ConversationViewHolder conViewHolder;
//
//        if (convertView == null) {
//            //inflate view
//            convertView = inflater.inflate(layoutId, parent, false);
//            conViewHolder = new ConversationViewHolder();
//            conViewHolder.setAvatar((ImageView) convertView.findViewById(R.id.iv_avatar));
//            conViewHolder.setMessage((TextView) convertView.findViewById(R.id.tv_message));
//
//            convertView.setTag(conViewHolder);
//        }
//
//        Picasso.with(BootstrapApplication.getInstance())
//                .load("dummy_avatar_url")
//                .placeholder(R.drawable.gravatar_icon)
//                .into((ImageView)convertView.findViewById(R.id.iv_avatar));
//        //convertView.findViewById(R.id.iv_avatar).setBackgroundColor(Color.BLUE);
//
////        String name = msgItem.getFromUser().getUsername();
//        String name = "user1@sageburner.com";
//        String msgText = msgItem.getMessageText();
//        ((TextView)convertView.findViewById(R.id.tv_message)).setText(msgText);
//
//        return convertView;
//    }
}