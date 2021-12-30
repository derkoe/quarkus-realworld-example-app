package realworld.security;

import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.ws.rs.core.SecurityContext;
import java.util.UUID;

public class UserHelper {
    private UserHelper() {
    }

    public static UUID getUserId(SecurityContext sec) {
        if (sec.getUserPrincipal() instanceof JsonWebToken userPrincipal) {
            return UUID.fromString(userPrincipal.getSubject());
        }
        return null;
    }
}
