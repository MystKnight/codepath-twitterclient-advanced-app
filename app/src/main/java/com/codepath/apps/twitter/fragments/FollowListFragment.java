package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.models.FollowAdapter;
import com.codepath.apps.twitter.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yahuijin on 10/6/15.
 */
public class FollowListFragment extends Fragment {

    private List<User> users;
    private ListView lvFollow;
    private FollowAdapter followAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Attempt to fetch tweets from our offline storage
        this.users = new ArrayList<>();
        this.followAdapter = new FollowAdapter(getContext(), this.users);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_list, container, false);

        // Set up the list view
        this.lvFollow = (ListView)view.findViewById(R.id.lv_follow_list);
        this.lvFollow.setAdapter(this.followAdapter);

        return view;
    }

    public void addAll(List<User> users) {
        this.followAdapter.addAll(users);
    }
}
