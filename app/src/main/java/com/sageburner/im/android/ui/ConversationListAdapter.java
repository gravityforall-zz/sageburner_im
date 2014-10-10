
package com.sageburner.im.android.ui;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.sageburner.im.android.BootstrapApplication;
import com.sageburner.im.android.R;
import com.sageburner.im.android.R.drawable;
import com.sageburner.im.android.core.ConversationViewHolder;
import com.sageburner.im.android.core.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter to
 */
public class ConversationListAdapter extends AlternatingColorListAdapter<User> {

    private final LayoutInflater inflater;

    /**
     * @param inflater
     * @param items
     */
    public ConversationListAdapter(final LayoutInflater inflater, final List<User> items) {
        super(R.layout.user_list_item, inflater, items);

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
    protected void update(final int position, final User user) {
        super.update(position, user);

        Picasso.with(BootstrapApplication.getInstance())
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.gravatar_icon)
                .into(imageView(0));

        setText(1, String.format("%1$s %2$s", user.getFirstName(), user.getLastName()));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int layoutId;
        if (position % 2 != 0) {
            layoutId = R.layout.conversation_list_item_out;
        } else {
            layoutId = R.layout.conversation_list_item_in;
        }

        //notes
        ConversationViewHolder conViewHolder;

        if (convertView == null) {
            // Inflate your view
            convertView = inflater.inflate(layoutId, parent, false);
            conViewHolder = new ConversationViewHolder();
            conViewHolder.setAvatar((ImageView) convertView.findViewById(R.id.iv_avatar));
            conViewHolder.setMessage((TextView) convertView.findViewById(R.id.tv_message));

            convertView.setTag(conViewHolder);
        }

        Picasso.with(BootstrapApplication.getInstance())
                .load(1)
                .placeholder(R.drawable.gravatar_icon)
                .into((ImageView)convertView.findViewById(R.id.iv_avatar));
        //convertView.findViewById(R.id.iv_avatar).setBackgroundColor(Color.BLUE);
        ((TextView)convertView.findViewById(R.id.tv_message)).setText("");

        return convertView;
    }
}