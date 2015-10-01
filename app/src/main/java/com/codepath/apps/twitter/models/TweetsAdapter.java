package com.codepath.apps.twitter.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.network.TwitterApplication;
import com.codepath.apps.twitter.network.TwitterClient;
import com.codepath.apps.twitter.utils.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by yahuijin on 9/17/15.
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> {

    Context context;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
        this.context = context;
    }

    // Todo: ViewHolder pattern
    private static class ViewHolder {
        TextView tvScreenName;
        TextView tvName;
        TextView tvBody;
        TextView tvCreateDate;

        Button btnReply;
        Button btnRetweet;
        Button btnFavorite;

        ImageView ivAvatar;
        ImageView ivMedia;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);

            // Only look-up once
            viewHolder = new ViewHolder();
            viewHolder.ivAvatar = (ImageView)convertView.findViewById(R.id.iv_avatar);
            viewHolder.ivMedia = (ImageView)convertView.findViewById(R.id.iv_media);
            viewHolder.tvScreenName = (TextView)convertView.findViewById(R.id.tv_screen_name);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.tvBody = (TextView)convertView.findViewById(R.id.tv_body);
            viewHolder.tvCreateDate = (TextView)convertView.findViewById(R.id.tv_create_date);
            viewHolder.btnReply = (Button)convertView.findViewById(R.id.btn_reply);
            viewHolder.btnRetweet = (Button)convertView.findViewById(R.id.btn_retweet);
            viewHolder.btnFavorite = (Button)convertView.findViewById(R.id.btn_favorite);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Do not show media asset by default
        viewHolder.ivMedia.setVisibility(View.GONE);
        // If we have a valid media, let's show it
        if (tweet.getMedia() != null) {
            Picasso.with(getContext()).load(tweet.getMedia().getMediaUrl()).into(viewHolder.ivMedia);
            viewHolder.ivMedia.setVisibility(View.VISIBLE);
        }

        // Set the avatar of the person tweeting
        viewHolder.ivAvatar.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivAvatar);

        // Set text
        viewHolder.tvName.setText(tweet.getUser().getName());
        viewHolder.tvScreenName.setText(tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getText());
        viewHolder.tvCreateDate.setText(tweet.getSmartDate());

        // Set button positions so we can get back at the tweet when the button is clicked
        viewHolder.btnReply.setTag(position);
        viewHolder.btnRetweet.setTag(position);
        viewHolder.btnFavorite.setTag(position);

        // Set the button text if we have any sort of valid counts
        viewHolder.btnRetweet.setText("");
        if (tweet.getRetweetCount() != 0) {
            String tweetCount = Integer.toString(tweet.getRetweetCount());
            viewHolder.btnRetweet.setText(tweetCount);
        }

        viewHolder.btnFavorite.setText("");
        if (tweet.getFavoriteCount() != 0) {
            String favoriteCount = Integer.toString(tweet.getFavoriteCount());
            viewHolder.btnFavorite.setText(favoriteCount);
        }

        // Set the button states depending on if we have favorited or retweeted before
        viewHolder.btnFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_favorite, 0, 0, 0);
        if (tweet.getFavorited()) {
            viewHolder.btnFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_favorite_pressed, 0, 0, 0);
            // Looks like there is an inconsistency in the api where my post was favorited but favorite count is 0
            if (tweet.getFavoriteCount() == 0) {
                viewHolder.btnFavorite.setText("1");
            }
        }

        // If the tweet is from the current user then you cannot retweet your own things
        viewHolder.btnRetweet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_retweet, 0, 0, 0);
        viewHolder.btnRetweet.setEnabled(true);
        if (tweet.getUser().getScreenName().equals("@onionpixel")) {
            viewHolder.btnRetweet.setEnabled(false);
            // Force light gray since set enabled sets it to black
            viewHolder.btnRetweet.setTextColor(this.context.getResources().getColor(R.color.light_gray));
            viewHolder.btnRetweet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_retweet_disabled, 0, 0, 0);
        }

        // Make sure we don't highlight our own retweet
        if (tweet.getRetweeted()) {
            // We should be able to un-retweet
            viewHolder.btnRetweet.setEnabled(true);
            viewHolder.btnRetweet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_retweet_pressed, 0, 0, 0);
        }

        return convertView;
    }
}
