package realworld;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import realworld.users.UserRegistration;
import realworld.users.web.UserRequest;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class UserTest {

    @Test
    public void testUserRegistration() {
        UserRequest<UserRegistration> request = new UserRequest<>();
        UserRegistration registration = new UserRegistration();
        registration.username = "test";
        registration.email = "test@example.com";
        registration.password = "test123";
        request.user = registration;
        given().when().body(request).contentType(ContentType.JSON).post("/api/users").then().statusCode(201);
    }
}
