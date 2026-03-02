package com.example.Repetition_7;

import com.example.Repetition_7.entity.UserEntity;
import com.example.Repetition_7.entity.roles.UserRole;
import com.example.Repetition_7.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@AutoConfigureMockMvc
public class AuthFlowIT extends BaseIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final static String USERNAME = "testuser";
    private final static String PASSWORD = "password123";

    @BeforeEach
    public void setUpUser() {
        userRepository.findByUsername(USERNAME).ifPresent(entity -> userRepository.delete(entity));

        UserEntity user = new UserEntity();
        user.setUsername(USERNAME);
        user.setPasswordHash(passwordEncoder.encode(PASSWORD));
        user.setRole(UserRole.USER);
        userRepository.save(user);
        userRepository.flush();
    }

    @Test
    public void login_ShouldReturnAccessToken_andRefreshCookie() throws Exception {
       AuthTokens tokens = doLogin(USERNAME, PASSWORD);

        assertThat(tokens.accessToken()).isNotBlank();
        assertThat(tokens.refreshCookie()).isNotNull();
        assertThat(tokens.refreshCookie().isHttpOnly()).isTrue();
    }

    @Test
    public void refresh_ShouldRotateRefreshCookie_andReturnNewAccessToken() throws Exception {
        AuthTokens first = doLogin(USERNAME, PASSWORD);
        AuthTokens second = doRefresh(first.refreshCookie());

        assertThat(second.accessToken()).isNotBlank();
        assertThat(second.refreshCookie()).isNotNull();

        assertThat(second.refreshCookie().getValue()).isNotEqualTo(first.refreshCookie().getValue());
    }

    private AuthTokens doLogin(String username, String password) throws Exception {
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", containsString("refreshToken=")))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Cookie refresh = result.getResponse().getCookie("refreshToken");

        if(refresh.getValue().isBlank() || !refresh.getPath().equals("/api/auth")) {
            throw new AssertionError("Invalid refresh token cookie: " + refresh);
        }

        String access = extractAccessToken(responseBody);

        return new AuthTokens(access, refresh);
    }

    private AuthTokens doRefresh(Cookie refreshCookie) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/refresh")
                .cookie(refreshCookie))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", containsString("refreshToken=")))
                .andReturn();

        Cookie newRefresh = result.getResponse().getCookie("refreshToken");
        String newAccess = extractAccessToken(result.getResponse().getContentAsString());

        return new AuthTokens(newAccess, newRefresh);
    }

    private String extractAccessToken(String responseBody) {
        String key = "\"accessToken\":\"";
        int start = responseBody.indexOf(key);
        if(start == -1) {
            throw new AssertionError("accessToken not found in response");
        }
        start += key.length();

        int end = responseBody.indexOf('"', start);
        if(end == -1) {
            throw new AssertionError("malformed JSON: " + responseBody);
        }

        return responseBody.substring(start, end);
    }

    private record AuthTokens(String accessToken, Cookie refreshCookie) {}

}
