package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.models.User;
import com.codepath.apps.twitter.network.TwitterApplication;
import com.codepath.apps.twitter.network.TwitterClient;
import com.codepath.apps.twitter.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yahuijin on 10/6/15.
 */
public class FollowingFragment extends FollowListFragment {

    private User user;
    private TwitterClient client;

    public static FollowingFragment newInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);

        FollowingFragment followingFragment = new FollowingFragment();
        followingFragment.setArguments(bundle);

        return followingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.user = getArguments().getParcelable("user");

        // Make the initial network call to populate the timeline
        this.client = TwitterApplication.getRestClient();
        this.populateFollowing();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void populateFollowing() {
        if (!Utils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            return;
        }

        this.client.getFollowing(user.getScreenName(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                try {
                    JSONArray rawData = response.getJSONArray("users");
                    List<User> users = User.fromJSONArray(rawData);
                    addAll(users);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                Toast.makeText(getActivity(), getResources().getString(R.string.fail_fetch_following), Toast.LENGTH_LONG).show();
            }
        });
    }
}
