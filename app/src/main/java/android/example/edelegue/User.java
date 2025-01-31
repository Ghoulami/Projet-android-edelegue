package android.example.edelegue;
public class User {
    private String id;
    private String username;
    private String Email;
    private String profile;
    private String imageURL;
    private String status;
    private String search;

    public User(String id, String username, String email, String profile, String imageURL, String status, String search) {
        this.id = id;
        this.username = username;
        this.Email = email;
        this.profile = profile;
        this.imageURL = imageURL;
        this.status = status;
        this.search = search;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
