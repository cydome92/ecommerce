package dev.domenicozagaria.ecommerce;

import dev.domenicozagaria.ecommerce.configuration.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class EcommerceApplicationTests {

	@Test
	void contextLoads() {
	}

}
