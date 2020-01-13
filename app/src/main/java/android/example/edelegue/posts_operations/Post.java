package android.example.edelegue.posts_operations;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
public class Post {
    String title , content , img_url , user_id;
    Timestamp currentTime ;


    public Post(String title, String content, String img_url, String user_id, Timestamp currentTime) {
        this.title = title;
        this.content = content;
        this.img_url = img_url;
        this.user_id = user_id;
        this.currentTime = currentTime;
    }

    public void setCurrentTime(Timestamp currentTime) {
        this.currentTime = currentTime;
    }
    public Timestamp getCurrentTime() {
        return currentTime;
    }

    public Post(){

    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
