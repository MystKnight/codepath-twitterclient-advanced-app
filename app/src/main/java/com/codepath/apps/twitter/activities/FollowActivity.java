package com.codepath.apps.twitter.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.fragments.FollowerFragment;
import com.codepath.apps.twitter.fragments.FollowingFragment;
import com.codepath.apps.twitter.models.User;

public class FollowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        // Get the user from the intent
        User user = getIntent().getParcelableExtra("user");
        String type = getIntent().getStringExtra("type");

        // Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (type.equals("followers")) {
            // Followers
            FollowerFragment followerFragment = FollowerFragment.newInstance(user);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_follow, followerFragment);
            fragmentTransaction.commit();

            getSupportActionBar().setTitle(R.string.followers);
        } else {
            // Following
            FollowingFragment followingFragment = FollowingFragment.newInstance(user);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_follow, followingFragment);
            fragmentTransaction.commit();

            getSupportActionBar().setTitle(R.string.following);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return false;
    }
}
