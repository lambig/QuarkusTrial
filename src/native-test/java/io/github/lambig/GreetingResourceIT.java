package io.github.lambig;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
class GreetingResourceIT extends GreetingResourceTest {
    @Test
    void testGreeting() {
        super.testGreeting(); // 要らないはずだけど書いとかないと出力が出ない
    }
}