package com.sageburner.im.android.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import com.sageburner.im.android.BootstrapApplication;
import com.sageburner.im.android.R;
import com.sageburner.im.android.core.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adapter to display a list of friends
 */
public class ConversationAdapter extends AlternatingColorListAdapter<User> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd");

    /**
     * @param inflater
     * @param items
     */
    public ConversationAdapter(final LayoutInflater inflater, final List<User> items) {
        super(R.layout.user_list_item, inflater, items);

        //setItems(items);
    }

    /**
     * @param inflater
     */
    public ConversationAdapter(final LayoutInflater inflater) {
        this(inflater, null);

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

        Picasso.with(BootstrapApplication.getInstance())
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.gravatar_icon)
                .into(imageView(0));

        setText(1, String.format("%1$s %2$s", user.getFirstName(), user.getLastName()));

    }

}
