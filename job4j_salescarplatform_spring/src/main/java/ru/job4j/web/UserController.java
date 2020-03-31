package ru.job4j.web;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Car;
import ru.job4j.domain.JsonResponse;
import ru.job4j.domain.User;
import ru.job4j.service.ServiceInterface;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * the controller for requests tied to User.
 */
@Controller
public class UserController {

    @Autowired
    public UserController(final ServiceInterface service, final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.service = service;
    }

    /**
     * The password encoder.
     */
    private PasswordEncoder passwordEncoder;
    /**
     * the service layer.
     */
    private final ServiceInterface service;

    /**
     * returns the registration form for filling in the new user data.
     *
     * @return registration form.
     */
    @GetMapping(value = "/registration")
    public String showRegistrationPage() {
        return "registration";
    }

    /**
     * processes the adding of the new user to th data base.
     *
     * @param jsonString - the json request with user data
     * @return the json response.
     * @throws IOException
     */
    @PostMapping(value = "/registration", consumes = "application/json", produces = "application/json")
    public @ResponseBody
    String register(@RequestBody String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(jsonString, User.class);
        int id = this.service.addUser(user.toUser(this.passwordEncoder));
        String status = "invalid";
        if (id != -1) {
            status = "valid";
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String resultJSON = ow.writeValueAsString(new JsonResponse(status));
        return resultJSON;
    }

    /**
     * loads the user cabinet page or forwards to the page for signing in.
     *
     * @param modelMap - the map for response, the method attaches cars list
     *                 and user name to it.
     * @param session  - HttpSession
     * @return page for signing in or page of user cabinet.
     */
    @GetMapping(value = "/cabinet")
    public String showCabinet(ModelMap modelMap, HttpSession session) {
        int id = (Integer) session.getAttribute("id");
        String name = (String) session.getAttribute("name");
        List<Car> list = this.service.loadByUser(new User(id));
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("name", name);
        return "cabinet";
    }
}

