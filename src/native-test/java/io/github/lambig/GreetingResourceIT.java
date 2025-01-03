package io.github.lambig;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
class GreetingResourceIT extends GreetingResourceTest {
    // Execute the same tests but in packaged mode.
    @Test
    void testGreeting() {}

}
