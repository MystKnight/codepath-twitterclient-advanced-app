package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.models.Tweet;
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
public class SearchTweetsFragment extends TweetsListFragment {

    private final String TYPE = "search";
    private TwitterClient client;

    public static SearchTweetsFragment newInstance(String query) {
        Bundle bundle = new Bundle();
        bundle.putString("query", query);

        SearchTweetsFragment searchTweetsFragment = new SearchTweetsFragment();
        searchTweetsFragment.setArguments(bundle);

        return searchTweetsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Clear the list
        clear();

        // Get the query
        String query = getArguments().getString("query");

        // Make the initial network call to populate the timeline
        this.client = TwitterApplication.getRestClient();
        this.searchTweets(query);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    private void searchTweets(String query) {
        if (!Utils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            return;
        }

        this.client.searchTweets(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray rawData = null;
                try {
                    rawData = response.getJSONArray("statuses");
                    // Process the tweets and add it to our adapter
                    List<Tweet> tweets = Tweet.fromJSONArray(rawData, TYPE);
                    // Add the tweets and hide the refresh button
                    addAll(tweets);
                    // Scroll to top
                    getTweetListView().setSelectionAfterHeaderView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                getSwipeContainer().setRefreshing(false);
                Toast.makeText(getActivity(), getResources().getString(R.string.fail_search), Toast.LENGTH_LONG).show();
            }
        });
    }
}
