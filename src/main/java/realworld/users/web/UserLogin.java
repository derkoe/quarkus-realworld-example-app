package realworld.users.web;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserLogin {
    @NotBlank
    @Email
    public String email;
    @NotBlank
    public String password;
}
