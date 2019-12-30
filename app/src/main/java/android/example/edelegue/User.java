package android.example.edelegue;

public class User {
    private String userName, email, profile;

    public User(String userName, String email, String profile) {
        this.userName = userName;
        this.email = email;
        this.profile = profile;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile() {
        return profile;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
