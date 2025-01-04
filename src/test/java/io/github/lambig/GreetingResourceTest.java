package io.github.lambig;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.matchesPattern;

@QuarkusTest
class GreetingResourceTest {
    @Test
    void testGreeting() {
        IntStream
                .range(1, 11)
                .forEach(index -> {
                            long startTime = System.nanoTime();
                            var response = given()
                                    .when().get("/hello");
                            response.then()
                                    .statusCode(200);
                            long endTime = System.nanoTime();
                            System.out.println("テスト結果" + index + ":");
                            System.out.println(response.getBody().asString());
                            System.out.println("#### Test Execution time: " + (endTime - startTime) / 1_000_000 + " ms ####");
                        }
                );
    }
}