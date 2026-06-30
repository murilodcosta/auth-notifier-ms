package dev.murilodcosta.auth_service;

import dev.murilodcosta.auth_service.domain.entities.User;
import dev.murilodcosta.auth_service.domain.enums.Role;
import dev.murilodcosta.auth_service.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		userRepository.findByEmail("test-user@example.com").ifPresent(userRepository::delete);
		userRepository.findByEmail("test-admin@example.com").ifPresent(userRepository::delete);

		userRepository.save(User.builder()
				.name("Test User")
				.email("test-user@example.com")
				.password(passwordEncoder.encode("password"))
				.role(Role.USER)
				.build());

		userRepository.save(User.builder()
				.name("Test Admin")
				.email("test-admin@example.com")
				.password(passwordEncoder.encode("password"))
				.role(Role.ADMIN)
				.build());
	}

	@AfterEach
	void tearDown() {
		userRepository.findByEmail("test-user@example.com").ifPresent(userRepository::delete);
		userRepository.findByEmail("test-admin@example.com").ifPresent(userRepository::delete);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void loginWithInvalidCredentials_ShouldReturn400() throws Exception {
		mockMvc.perform(post("/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"nonexistent@gmail.com\",\"password\":\"wrongpassword\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertEquals("Invalid credentials", result.getResponse().getErrorMessage()));
	}

	@Test
	void getUsersAsUser_ShouldReturn403() throws Exception {
		var userDetails = userRepository.findByEmail("test-user@example.com").orElseThrow();
		mockMvc.perform(get("/v1/users")
						.with(user(userDetails)))
				.andExpect(status().isForbidden());
	}

	@Test
	void getUsersAsAdmin_ShouldReturn200() throws Exception {
		var userDetails = userRepository.findByEmail("test-admin@example.com").orElseThrow();
		mockMvc.perform(get("/v1/users")
						.with(user(userDetails)))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	void getMyUser_ShouldReturnProfile() throws Exception {
		var userDetails = userRepository.findByEmail("test-user@example.com").orElseThrow();
		mockMvc.perform(get("/v1/users/me")
						.with(user(userDetails)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("test-user@example.com"))
				.andExpect(jsonPath("$.username").value("test-user@example.com"))
				.andExpect(jsonPath("$.role").value("USER"));
	}

}
