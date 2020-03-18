package ru.job4j.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.job4j.models.Car;
import ru.job4j.models.JsonResponse;
import ru.job4j.models.User;
import ru.job4j.service.ServiceInterface;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * the controller for requests tied to User.
 */
@Controller
@Component
public class UserController {

    @Autowired
    public UserController(final ServiceInterface service) {
        this.service = service;
    }

    /**
     * the service layer.
     */
    private final ServiceInterface service;

    /**
     * returns the registration form for filling in the new user data.
     * @return registration form.
     */
    @GetMapping(value = "/registration")
    public String showRegistrationPage() {
        return "registration";
    }

    /**
     * processes the adding of the new user to th data base.
     * @param jsonString - the json request with user data
     * @param session HttpSession for adding user name and id.
     * @return the json response.
     * @throws IOException
     */
    @PostMapping(value = "/registration", consumes = "application/json", produces = "application/json")
    public @ResponseBody String register(@RequestBody String jsonString, HttpSession session) throws IOException {
        return commonWrapper(jsonString, session, this.service::addUser);
    }

    /**
     * returns the page for signing in.
     * @return sign in page
     */
    @GetMapping(value = "/signin")
    public String showSignInPage() {
        return "signin";
    }

    /**
     * processes the checking of the user credential. If the user exists in the
     * data base, than sets name and id of the user to session.
     * @param jsonString - the json request
     * @param session - HttpSession
     * @return - json response
     * @throws IOException
     */
    @PostMapping(value = "/signin", consumes = "application/json", produces = "application/json")
    public @ResponseBody String checkCredential(@RequestBody String jsonString, HttpSession session) throws IOException {
        return commonWrapper(jsonString, session, this.service::isCredential);
    }

    private String commonWrapper(String jsonString, HttpSession session, Function<User, Integer> operation) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(jsonString, User.class);
        int id = operation.apply(user);
        String status = "invalid";
        if (id != -1) {
            synchronized (session) {
                session.setAttribute("name", user.getName());
                session.setAttribute("id", id);
            }
            status = "valid";
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String resultJSON = ow.writeValueAsString(new JsonResponse(status));
        return resultJSON;
    }

    /**
     * loads the user cabinet page or forwards to the page for signing in.
     * @param modelMap - the map for response, the method attaches cars list
     *                 and user name to it.
     * @param session - HttpSession
     * @return page for signing in or page of user cabinet.
     */
    @GetMapping(value = {"/login", "/cabinet"})
    public String showCabinet(ModelMap modelMap, HttpSession session) {
        Integer id = (Integer) session.getAttribute("id");
        if (id == null) {
            return "signin";
        }
        String name = (String) session.getAttribute("name");
        List<Car> list = this.service.loadByUser(new User(id));
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("name", name);
        return "cabinet";
    }
}

