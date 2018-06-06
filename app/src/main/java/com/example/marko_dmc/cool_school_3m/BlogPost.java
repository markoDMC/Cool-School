package com.example.marko_dmc.cool_school_3m;

import java.util.Date;
import com.google.firebase.firestore.ServerTimestamp;

public class BlogPost extends BlogPostId {

    public String user_id, image_url, desc, image_thumb, idSkole;
    public Date timestamp;

    public BlogPost() {
    }

    public BlogPost(String user_id, String image_url, String desc, String image_thumb,String idSkole, Date timestamp) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.desc = desc;
        this.image_thumb = image_thumb;
        this.idSkole=idSkole;
        this.timestamp = timestamp;
    }

    public String getIdSkole(){return idSkole; }

    public void setIdSkole(String idSkole){this.idSkole=idSkole;}

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}