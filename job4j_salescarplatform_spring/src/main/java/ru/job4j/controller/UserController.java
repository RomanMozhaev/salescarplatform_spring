package ru.job4j.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.node.TextNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.job4j.models.Car;
import ru.job4j.models.JsonResponse;
import ru.job4j.models.User;
import ru.job4j.service.ServiceInterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Component
public class UserController {

    @Autowired
    public UserController(final ServiceInterface service) {
        this.service = service;
    }

    private final ServiceInterface service;

    private static final Logger LOG = LogManager.getLogger(UserController.class.getName());


    @GetMapping(value = "/registration")
    public String showRegistrationPage() {
        return "registration";
    }

    //registration servlet
    @PostMapping(value = "/registration", consumes = "application/json", produces = "application/json")
    public @ResponseBody String register(@RequestBody String jsonString, HttpSession session) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(jsonString, User.class);
        int id = this.service.addUser(user);
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

    //signin servlet get
    @GetMapping(value = "/signin")
    public String showSignInPage() {
        return "signin";
    }

    //sing in servlet post
    @PostMapping(value = "/signin", consumes = "application/json", produces = "application/json")
    public @ResponseBody String checkCredential(@RequestBody String jsonString, HttpSession session) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(jsonString, User.class);
        int id = this.service.isCredential(user);
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

