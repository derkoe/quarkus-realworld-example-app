package realworld.users;

public class UserWithToken {
    public final String username;
    public final String email;
    public final String bio;
    public final String image;
    public final String token;

    UserWithToken(String username, String email, String bio, String image, String token) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.image = image;
        this.token = token;
    }
}
