package com.codepath.apps.twitter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.activities.FollowActivity;
import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.models.User;
import com.codepath.apps.twitter.network.TwitterApplication;
import com.codepath.apps.twitter.network.TwitterClient;
import com.codepath.apps.twitter.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yahuijin on 9/30/15.
 */
public class UserTimelineFragment extends TweetsListFragment {

    private final String TYPE = "usertimeline";
    private TwitterClient client;
    private User user;
    private Boolean requiresClearingAdapter = true;

    public static UserTimelineFragment newInstance(User user) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();

        Bundle args = new Bundle();
        args.putParcelable("user", user);
        userTimelineFragment.setArguments(args);

        return userTimelineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.user = getArguments().getParcelable("user");

        // Load offline content by default
        clear();
        addAll(Tweet.getTweetByUser(this.user, this.TYPE));

        this.client = TwitterApplication.getRestClient();
        this.populateUserTimeline();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Set listeners for scroll and on tap to go to details
        getTweetListView().setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                requiresClearingAdapter = false;
                Log.d("DEBUG - LOAD MORE", Integer.toString(page));
                populateUserTimeline();
            }
        });

        // On swipe, we want to notify the adapter it needs to be cleared before loading more info
        getSwipeContainer().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requiresClearingAdapter = true;
                populateUserTimeline();
            }
        });

        // Add the user info view as part of the list view
        View userInfoView = createListHeaderView(inflater, container);
        getTweetListView().addHeaderView(userInfoView);

        return view;
    }

    public void populateUserTimeline() {
        this.showProgressBar();

        this.client.getUserTimeline(this.user.getScreenName(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                hideProgressBar();

                // Got notified that we need to clear up our adapter
                if (requiresClearingAdapter) {
                    // We've just cleared so let's reset
                    requiresClearingAdapter = false;
                    // Clear our cached data
                    Tweet.clearTweetsByType(TYPE);
                    // Clear the adapter
                    clear();
                }

                // Process the tweets and add it to our adapter
                List<Tweet> tweets = Tweet.fromJSONArray(response, TYPE);
                // Add the tweets and hide the refresh button
                addAll(tweets);
                // Reset the swipe refresh bar if it is in progress
                getSwipeContainer().setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                getSwipeContainer().setRefreshing(false);
                Toast.makeText(getActivity(), getResources().getString(R.string.fail_fetch_usertimeline), Toast.LENGTH_LONG).show();            }
        });
    }

    private View createListHeaderView(LayoutInflater inflater, ViewGroup container) {
        View userInfoView = inflater.inflate(R.layout.fragment_user_info, container, false);

        RelativeLayout followingContainer = (RelativeLayout)userInfoView.findViewById(R.id.following_container);
        RelativeLayout followerContainer = (RelativeLayout)userInfoView.findViewById(R.id.follower_container);
        TextView tvName = (TextView)userInfoView.findViewById(R.id.tv_name);
        TextView tvScreenname = (TextView)userInfoView.findViewById(R.id.tv_screen_name);
        TextView tvLocation = (TextView)userInfoView.findViewById(R.id.tv_location);
        TextView tvFollowing = (TextView)userInfoView.findViewById(R.id.tv_following);
        TextView tvFollowers = (TextView)userInfoView.findViewById(R.id.tv_followers);
        TextView tvDescription = (TextView)userInfoView.findViewById(R.id.tv_description);
        ImageView ivAvatar = (ImageView)userInfoView.findViewById(R.id.iv_avatar);
        ImageView ivProfileBackground = (ImageView)userInfoView.findViewById(R.id.iv_profile_background);

        tvName.setText(this.user.getName());
        tvScreenname.setText(this.user.getDisplayScreenName());
        tvLocation.setText(this.user.getLocation());
        tvDescription.setText(this.user.getDescription());

        String follower = Long.toString(this.user.getFollowersCount());
        String following = Long.toString(this.user.getFollowingCount());
        tvFollowing.setText(following);
        tvFollowers.setText(follower);

        Picasso.with(getContext()).load(this.user.getProfileImageUrl()).into(ivAvatar);
        Picasso.with(getContext()).load(this.user.getProfileBackgroundImageUrl()).into(ivProfileBackground);

        followingContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FollowActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("type", "following");
                startActivity(intent);
            }
        });

        followerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FollowActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("type", "followers");
                startActivity(intent);
            }
        });

        return userInfoView;
    }
}
