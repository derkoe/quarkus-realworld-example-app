package realworld.users;

public class UserWithToken {
    public final String username;
    public final String email;
    public final String token;

    UserWithToken(String username, String email, String token) {
        this.username = username;
        this.email = email;
        this.token = token;
    }
}
