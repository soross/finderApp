package com.codepath.finderapp.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Data model for a post.
 */

@ParseClassName("Posts")
public class PicturePost extends ParseObject {

        public String getText() {
            return getString("text");
        }

        public void setText(String value) {
            put("text", value);
        }

        public ParseFile getImage() { return getParseFile("image"); }

        public void setImage(ParseFile image) { put("image", image); }

        /*public ParseUser getUser() {
            return getParseUser("user");
        }

        public void setUser(ParseUser value) {
            put("user", value);
        }*/

        public ParseGeoPoint getLocation() {
            return getParseGeoPoint("location");
        }

        public void setLocation(ParseGeoPoint value) {
            put("location", value);
        }

        public static ParseQuery<PicturePost> getQuery() {
            return ParseQuery.getQuery(PicturePost.class);
        }
}
