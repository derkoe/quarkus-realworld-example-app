package realworld;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class ArticlesTest {

    @Test
    public void testArticleList() throws Exception {
        given()
                .when().get("/api/articles")
                .then()
                .statusCode(200)
                .body(containsString("articles"));
    }

}
