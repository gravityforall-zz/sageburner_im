package com.sageburner.im.android.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sageburner.im.android.R;
import com.sageburner.im.android.core.User;
import com.squareup.picasso.Picasso;

import butterknife.InjectView;

import static com.sageburner.im.android.core.Constants.Extra.USER;

public class ConversationActivity extends BootstrapActivity {

    @InjectView(R.id.iv_avatar) protected ImageView avatar;
    @InjectView(R.id.tv_name) protected TextView name;

    private User user;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.user_view);
//
//        if (getIntent() != null && getIntent().getExtras() != null) {
//            user = (User) getIntent().getExtras().getSerializable(USER);
//        }
//
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        Picasso.with(this).load(user.getAvatarUrl())
//                .placeholder(R.drawable.gravatar_icon)
//                .into(avatar);
//
//        name.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_pages);
        viewPager.setCurrentItem(1, true);
    }
}
