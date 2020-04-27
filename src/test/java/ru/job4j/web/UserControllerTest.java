package ru.job4j.web;

import com.google.common.collect.Lists;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.job4j.domain.Car;
import ru.job4j.domain.JsonResponse;
import ru.job4j.domain.User;
import ru.job4j.repository.UserRepository;
import ru.job4j.service.ServiceInterface;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ServiceInterface service;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    public void whenShowRegistrationPageThenRegistrationPage() throws Exception {
        this.mvc.perform(get("/registration").accept(MediaType.TEXT_HTML))
                .andExpect(view().name("registration"));
    }

    @Test
    @WithMockUser(username = "User", password = "user")
    public void whenShowCabinetThenCabinet() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("name", "User");
        session.setAttribute("id", 10);
        Car car1 = new Car();
        Car car2 = new Car();
        int userId = 2;
        ArrayList<Car> list = new ArrayList<>(Lists.newArrayList(car1, car2));
        given(this.service.loadByUser(new User(userId)))
                .willReturn(list);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/cabinet")
                .session(session);
        this.mvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(model().attribute("list", hasSize(2)))
                .andExpect(model().attribute("name", "User"))
                .andExpect(view().name("cabinet"));
    }

    @Test
    @WithMockUser(username = "User", password = "user")
    public void whenRegisterThenJSON() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        UserForTest userForTest = new UserForTest("UserTest", "test");
        int userId = -1;
        String requestJson = mapper.writeValueAsString(userForTest);
        User user = new User();
        user.setUsername(userForTest.getUsername());
        user.setPassword(userForTest.getPassword());
        given(this.service.addUser(user.toUser(this.passwordEncoder)))
                .willReturn(userId);
        this.mvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(new JsonResponse("invalid"))));
    }

    private static class UserForTest {

        private String username;
        private String password;

        public UserForTest() {
        }

        public UserForTest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}