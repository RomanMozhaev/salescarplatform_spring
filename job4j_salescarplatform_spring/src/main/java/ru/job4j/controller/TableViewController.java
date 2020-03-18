package ru.job4j.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.List;
import java.util.Map;

@Controller
@Component
public class TableViewController {

    @Autowired
    public TableViewController(final ServiceInterface service) {
        this.service = service;
    }

    private final ServiceInterface service;

    private static final Logger LOG = LogManager.getLogger(TableViewController.class.getName());

    //start page servlet
    @GetMapping(value = "/")
    public String showStartingPage(ModelMap modelMap, HttpSession session) {
        String name = (String) session.getAttribute("name");
        if (name == null) {
            name = "Log In";
        }
        List<Car> list = this.service.loadTable();
        List<String> brands = this.service.allBrands();
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("name", name);
        modelMap.addAttribute("day", false);
        modelMap.addAttribute("photo", false);
        modelMap.addAttribute("setBrand", "none");
        modelMap.addAttribute("brands", brands);
        return "table";
    }

    @GetMapping(value = "/filter")
    public String filter(@RequestParam Map<String, String> filter,
                         ModelMap modelMap, HttpSession session) {
        String name = (String) session.getAttribute("name");
        if (name == null) {
            name = "Log In";
        }
        String day = filter.get("day");
        String photo = filter.get("photo");
        String brand = filter.get("brand");
        boolean dayBool = stringToBoolean(day);
        boolean photoBool = stringToBoolean(photo);
        List<Car> list = this.service.filter(dayBool, photoBool, brand);
        List<String> brands = this.service.allBrands();
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("name", name);
        modelMap.addAttribute("day", day);
        modelMap.addAttribute("photo", photo);
        modelMap.addAttribute("setBrand", brand);
        modelMap.addAttribute("brands", brands);
        return "table";
    }

    private boolean stringToBoolean(String bool) {
        boolean result = false;
        if (bool.equals("true")) {
            result = true;
        }
        return result;
    }
}


