package com.codepath.apps.twitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yahuijin on 9/17/15.
 */
@Table(name = "Users")
public class User extends Model implements Parcelable {

    @Column(name = "user_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long userId;
    @Column(name = "following_count")
    private long followingCount;
    @Column(name = "followers_count")
    private long followersCount;
    @Column(name = "name")
    private String name;
    @Column(name = "screen_name")
    private String screenName;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @Column(name = "profile_background_image_url")
    private String profileBackgroundImageUrl;
    @Column(name = "location")
    private String location;
    @Column(name = "description")
    private String description;

    public User() {
        super();
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();

        try {
            // Convert to our own object
            user.userId = jsonObject.getLong("id");
            user.followersCount = jsonObject.getLong("followers_count");
            user.followingCount = jsonObject.getLong("friends_count");
            user.name = jsonObject.getString("name");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");

            if (jsonObject.has("profile_banner_url")) {
                user.profileBackgroundImageUrl = jsonObject.getString("profile_banner_url");
            } else {
                user.profileBackgroundImageUrl = jsonObject.getString("profile_background_image_url");
            }

            user.location = jsonObject.getString("location");
            user.description = jsonObject.getString("description");
            user.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static List<User> fromJSONArray(JSONArray jsonArray) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                // Convert from json
                JSONObject userJson = jsonArray.getJSONObject(i);
                User user = User.fromJSON(userJson);

                if (user != null) {
                    users.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return users;
    }

    public static User getCurrentUser() {
        List<User> userList = new Select().from(User.class).where("screen_name = 'onionpixel'").execute();
        User currentUser = new User();

        if (userList.size() > 0) {
            currentUser = userList.get(0);
        }

        return currentUser;
    }

    public long getUserId() {
        return userId;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public String getName() {
        return name;
    }

    public String getDisplayScreenName() {
        return String.format("@%s", screenName);
    }

    public String getScreenName() {
        return this.screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeLong(this.followingCount);
        dest.writeLong(this.followersCount);
        dest.writeString(this.name);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.profileBackgroundImageUrl);
        dest.writeString(this.location);
        dest.writeString(this.description);
    }

    protected User(Parcel in) {
        this.userId = in.readLong();
        this.followingCount = in.readLong();
        this.followersCount = in.readLong();
        this.name = in.readString();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.profileBackgroundImageUrl = in.readString();
        this.location = in.readString();
        this.description = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
