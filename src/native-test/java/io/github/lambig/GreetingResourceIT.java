package io.github.lambig;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.matchesPattern;

@QuarkusIntegrationTest
class GreetingResourceIT extends GreetingResourceTest {
    // Execute the same tests but in packaged mode.
    @Test
    void testGreeting() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(allOf(
                        matchesPattern("\\d+"),  // 1つ以上の数字のみ
                        not(startsWith("-"))     // 負の数ではない
                ));
    }
}
