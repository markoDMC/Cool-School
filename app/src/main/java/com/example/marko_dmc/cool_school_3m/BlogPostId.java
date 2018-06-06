package com.example.marko_dmc.cool_school_3m;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;
import java.util.Date;

public class BlogPostId {

    @Exclude
    public String BlogPostId;

    public <T extends BlogPostId> T withId(@NonNull final String id) {
        this.BlogPostId = id;
        return (T) this;
    }

}
