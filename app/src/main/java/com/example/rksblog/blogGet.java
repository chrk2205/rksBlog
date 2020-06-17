package com.example.rksblog;

public class blogGet {

     public String blogimage;
     public String userid;
     public String caption;
     public String userimage;
  //   public String timestamp;

    //public String getTimestamp() {
     //   return timestamp;
    //}

  //  public void setTimestamp(String timestamp) {
   //     this.timestamp = timestamp;
    //}

    public blogGet() {
    }

    public blogGet(String blogimage, String userid, String caption) {
        this.blogimage = blogimage;
        this.userid = userid;
        this.caption = caption;
    }

    public String getBlogimage() {
        return blogimage;
    }

    public void setBlogimage(String blogimage) {
        this.blogimage = blogimage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }
}
