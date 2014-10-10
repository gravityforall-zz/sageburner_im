
package com.sageburner.im.android.ui;

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
 * List adapter that
 *
 * @param <V>
 */
public class ConversationListAdapter<V> extends
        SingleTypeAdapter<V> {

    private final LayoutInflater inflater;


    public ConversationListAdapter(final LayoutInflater inflater, final List<User> items) {
        super(inflater, 0);

        this.inflater = inflater;

        setItems(items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[0];
    }

    @Override
    protected void update(int i, V v) {
        int layoutId;
        if (i % 2 != 0) {
            layoutId = R.layout.conversation_list_item_out;
        } else {
            layoutId = R.layout.conversation_list_item_in;
        }
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
        } else {
            conViewHolder = (ConversationViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
