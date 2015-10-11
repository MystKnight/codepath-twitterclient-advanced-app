package com.codepath.apps.twitter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.activities.FollowActivity;
import com.codepath.apps.twitter.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by yahuijin on 10/5/15.
 */
public class UserProfileFragment extends Fragment {

    private User user;

    public static UserProfileFragment newInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);

        UserProfileFragment userProfileFragment = new UserProfileFragment();
        userProfileFragment.setArguments(bundle);

        return userProfileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.user = getArguments().getParcelable("user");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        TextView tvName = (TextView)view.findViewById(R.id.tv_name);
        TextView tvScreenname = (TextView)view.findViewById(R.id.tv_screen_name);
        TextView tvLocation = (TextView)view.findViewById(R.id.tv_location);
        TextView tvFollowing = (TextView)view.findViewById(R.id.tv_following);
        TextView tvFollowers = (TextView)view.findViewById(R.id.tv_followers);
        ImageView ivAvatar = (ImageView)view.findViewById(R.id.iv_avatar);

        tvName.setText(this.user.getName());
        tvScreenname.setText(this.user.getDisplayScreenName());
        tvLocation.setText(this.user.getLocation());
        tvFollowing.setText(Long.toString(this.user.getFollowingCount()));
        tvFollowers.setText(Long.toString(this.user.getFollowersCount()));
        Picasso.with(getContext()).load(this.user.getProfileImageUrl()).into(ivAvatar);

        tvFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FollowActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("type", "following");
                startActivity(intent);
            }
        });

        tvFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FollowActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("type", "followers");
                startActivity(intent);
            }
        });

        return view;
    }
}
