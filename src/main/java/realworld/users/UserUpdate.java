package realworld.users;

import javax.validation.constraints.Email;

public class UserUpdate {
    @Email
    public String email;
    public String password;
    public String username;
    public String bio;
    public String image;
}
