package realworld.users.web;

import realworld.users.UserWithToken;

class UserResponse {
    public final UserWithToken user;

    public UserResponse(UserWithToken user) {
        this.user = user;
    }
}
