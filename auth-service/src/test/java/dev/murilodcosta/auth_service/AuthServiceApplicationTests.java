package dev.murilodcosta.auth_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

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
	@WithMockUser(roles = "USER")
	void getUsersAsUser_ShouldReturn403() throws Exception {
		mockMvc.perform(get("/v1/users"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void getUsersAsAdmin_ShouldReturn200() throws Exception {
		mockMvc.perform(get("/v1/users"))
				.andExpect(status().isOk())
				.andExpect(content().string("List of users"));
	}

}
