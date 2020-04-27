package ru.job4j.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Car;
import ru.job4j.service.ServiceInterface;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * the controller for table displaying.
 */
@Controller
public class TableViewController {

    @Autowired
    public TableViewController(final ServiceInterface service) {
        this.service = service;
    }

    /**
     * the service layer.
     */
    private final ServiceInterface service;

    private static final Logger LOG = LogManager.getLogger(TableViewController.class.getName());

    /**
     * returns the main page with the main table of car tickets.
     *
     * @param model   - the map for response. The method attaches cars list,
     *                name, filter attributes, cars brands list.
     * @param session - HttpSession.
     * @return table page.
     */
    @GetMapping(value = "/")
    public String showStartingPage(ModelMap model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        String title;
        if (name == null) {
            title = "Log In";
        } else {
            title = name + ", your personal cabinet";
        }
        List<Car> list = this.service.loadTable();
        List<String> brands = this.service.allBrands();
        model.addAttribute("list", list);
        model.addAttribute("title", title);
        model.addAttribute("day", false);
        model.addAttribute("photo", false);
        model.addAttribute("setBrand", "none");
        model.addAttribute("brands", brands);
        return "table";
    }

    /**
     * returns the main page with applying of the tickets filters.
     *
     * @param filter   - the map with required filters.
     * @param modelMap - the map for response.The method attaches cars list,
     *                 name, filter attributes, cars brands list.
     * @param session  - HttpSession.
     * @return table page.
     */
    @GetMapping(value = "/filter")
    public String filter(@RequestParam Map<String, String> filter,
                         ModelMap modelMap, HttpSession session) {
        String name = (String) session.getAttribute("name");
        String title;
        if (name == null) {
            title = "Log In";
        } else {
            title = name + ", your personal cabinet";
        }
        String day = filter.get("day");
        String photo = filter.get("photo");
        String brand = filter.get("brand");
        boolean dayBool = stringToBoolean(day);
        boolean photoBool = stringToBoolean(photo);
        List<Car> list = this.service.filter(dayBool, photoBool, brand);
        List<String> brands = this.service.allBrands();
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("title", title);
        modelMap.addAttribute("day", day);
        modelMap.addAttribute("photo", photo);
        modelMap.addAttribute("setBrand", brand);
        modelMap.addAttribute("brands", brands);
        return "table";
    }

    /**
     * the method for converting string to boolean
     *
     * @param bool true or false as String
     * @return true or false as boolean.
     */
    private boolean stringToBoolean(String bool) {
        boolean result = false;
        if (bool.equals("true")) {
            result = true;
        }
        return result;
    }
}


