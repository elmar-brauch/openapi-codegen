package de.bsi.openapi.petstore;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.bsi.openapi.petstore.model.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/user")
@Slf4j
public class UserApiController {
	
	private List<User> users = new ArrayList<>();

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Mono<Void> createUser(@Valid @RequestBody User body) {
		return Mono.fromRunnable(() -> { 
			log.info("Creating user: " + body);
			users.add(body); 
		});
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	public Mono<User> findUserById(@RequestParam(required = true) final long id) {
		return Mono.fromSupplier(() -> {
			log.info("Searching user with id: " + id);
			return users.stream()
					.filter(u -> u.getId() == id)
					.findFirst().orElseThrow(UserNotFoundException::new);
		});
	}

}
