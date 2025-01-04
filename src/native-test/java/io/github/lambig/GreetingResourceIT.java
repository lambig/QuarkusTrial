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
        long startTime = System.nanoTime();
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(allOf(
                        matchesPattern("\\d+"),  // 1つ以上の数字のみ
                        not(startsWith("-"))     // 負の数ではない
                ));
        long endTime = System.nanoTime();
        System.out.println("#### Test Execution time: " + (endTime - startTime) / 1_000_000 + " ms ####");
    }
}