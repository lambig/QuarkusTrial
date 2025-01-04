package io.github.lambig;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.matchesPattern;

@QuarkusTest
class GreetingResourceTest {
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