package realworld.users;

import javax.validation.constraints.Email;

public record UserUpdate(@Email String email, String password, String username, String bio, String image) {
}
