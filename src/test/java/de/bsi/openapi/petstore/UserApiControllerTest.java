package de.bsi.openapi.petstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = UserApiController.class)
class UserApiControllerTest {

	@Autowired WebTestClient testClient;
	
	String userUri = "/v1/user";
	
	long testId = 123;
	String testMail = "hans@wurst.de";
	String testUser = String.format(
			"{ \"id\": %d, \"email\": \"%s\"}", 
			testId, testMail);
	
	@Test
	void testCreateAndReadUser() {
		testClient.post().uri(userUri)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(testUser)
				.exchange()
			.expectStatus().isCreated()
			.expectBody().isEmpty();
		
		testClient.get().uri(userUri + "?id=" + testId)
				.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.id").isEqualTo(testId)
			.jsonPath("$.email").isEqualTo(testMail);
	}
	
	@Test
	void testUserNotFound() {
		testClient.get().uri(userUri + "?id=777")
				.exchange()
			.expectStatus().isNotFound();
	}

}
