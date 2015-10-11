package com.codepath.apps.twitter.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.fragments.UserTimelineFragment;
import com.codepath.apps.twitter.models.User;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Get the user from the intent
        User user = getIntent().getParcelableExtra("user");

        // Show title
        getSupportActionBar().setTitle(user.getName());

        // Show back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Show the user fragment
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(user);
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.replace(R.id.fragment_user_container, userTimelineFragment);
        fragmentManager.commit();

        //UserContainerFragment userContainerFragment = UserContainerFragment.newInstance(user);
        //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.replace(R.id.fragment_user_container, userContainerFragment);
        //fragmentTransaction.commit();
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
