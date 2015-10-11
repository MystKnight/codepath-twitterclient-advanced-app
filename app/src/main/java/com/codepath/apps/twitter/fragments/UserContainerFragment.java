package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.models.User;

/**
 * Created by yahuijin on 10/5/15.
 */
public class UserContainerFragment extends Fragment {

    private User user;

    public static UserContainerFragment newInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);

        UserContainerFragment userContainerFragment = new UserContainerFragment();
        userContainerFragment.setArguments(bundle);

        return userContainerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.user = getArguments().getParcelable("user");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_container, container, false);

        // Dynamically load in the followers fragment
        UserProfileFragment userProfileFragment = UserProfileFragment.newInstance(this.user);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_followers, userProfileFragment);

        // Dynamically load in the user timeline
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(this.user);
        fragmentTransaction.replace(R.id.fragment_usertimeline, userTimelineFragment);

        // Commit the changes
        fragmentTransaction.commit();

        return view;
    }
}
