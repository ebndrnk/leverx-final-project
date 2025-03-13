package org.ebndrnk.leverxfinalproject.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignInRequest;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameRequest;
import org.ebndrnk.leverxfinalproject.model.entity.auth.Role;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.comment.Comment;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.repository.comment.CommentRepository;
import org.ebndrnk.leverxfinalproject.repository.pofile.ProfileRepository;
import org.ebndrnk.leverxfinalproject.service.comment.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.yaml")
public class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setMessage("test message");

        User user = new User();
        user.setEmailConfirmed(true);
        user.setEmail("email@email.com");
        user.setRole(Role.ROLE_SELLER);
        user.setPassword(passwordEncoder.encode("password"));
        user.setUsername("username");


        Profile profile = new Profile();
        profile.setFirstname("firstname");
        profile.setEmail("email@email.com");
        profile.setConfirmedByAdmin(false);
        profile.setUsername("username");



        Profile savedProfile = profileRepository.save(profile);
        user.setProfile(savedProfile);
        userRepository.save(user);

    }

    @Test
    public void testGetAllNotConfirmedUsers() throws Exception {
        mockMvc.perform(get("/admin/users/not-confirmed"))
                .andExpect(status().isForbidden());

    }


    @Test
    public void testAuthorizeAndPostGame() throws Exception {
        // 1. Авторизация
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("email@email.com");
        signInRequest.setPassword("password");

        String responseContent = mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 2. Парсинг JSON-ответа
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        String authToken = jsonNode.get("token").asText();

        // 2. Создание запроса для игры
        GameRequest gameRequest = new GameRequest();
        gameRequest.setTitle("Cyberpunk 2077");
        gameRequest.setText("Open-world RPG");
        gameRequest.setPrice(59.99F);

        // 3. Отправка запроса на создание игры
        mockMvc.perform(post("/game")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Cyberpunk 2077"))
                .andExpect(jsonPath("$.price").value(59.99));
    }
}
