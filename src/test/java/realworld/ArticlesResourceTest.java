package realworld;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class ArticlesResourceTest {

    @Test
    public void testArticleList() throws Exception {
        given()
          .when().get("/api/articles")
          .then()
             .statusCode(200)
             .body(is("[]"));
    }

}
