package ru.job4j.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.domain.Car;
import ru.job4j.domain.JsonResponse;
import ru.job4j.domain.User;
import ru.job4j.service.ServiceInterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Map;

/**
 * the controller for requests tied to Car.
 */
@Controller
public class CarController {

    @Autowired
    public CarController(final ServiceInterface service) {
        this.service = service;
    }

    /**
     * the service layer.
     */
    private final ServiceInterface service;

    /**
     * Logger
     */
    private static final Logger LOG = LogManager.getLogger(CarController.class.getName());

    /**
     * temp folder.
     */
    private static final String REPOSITORY = System.getProperty("java.io.tmpdir");

    /**
     * returns the page for the new car ticket form.
     *
     * @return adding page.
     */
    @GetMapping(value = "/add")
    public String addingPage() {
        return "addingPage";
    }

    /**
     * processes the adding a new car.
     *
     * @param jsonString - the json request with car ticket fields.
     * @param session    - HttpSession.
     * @return - the json response.
     * @throws IOException
     */
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public @ResponseBody
    String addCar(@RequestBody String jsonString, HttpSession session) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> carFields = mapper.readValue(jsonString, Map.class);
        Car car = new Car(carFields);
        int userId = (Integer) session.getAttribute("id");
        car.setUser(new User(userId));
        this.service.addCar(car);
        String status = "valid";
        String resultJSON = mapper.writeValueAsString(new JsonResponse(status));
        return resultJSON;
    }

    /**
     * changes car ticket status.
     *
     * @param jsonString - the json request with car id and new status.
     * @return - the json response.
     * @throws IOException
     */
    @PostMapping(value = "/change", consumes = "application/json", produces = "application/json")
    public @ResponseBody
    String changeStatus(@RequestBody String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> carFields = mapper.readValue(jsonString, Map.class);
        Car car = new Car(carFields);
        boolean result = this.service.changeStatus(car);
        String status = "invalid";
        if (result) {
            status = "valid";
        }
        String resultJSON = mapper.writeValueAsString(new JsonResponse(status));
        return resultJSON;
    }

    /**
     * downloads pictures.
     *
     * @param req  - HttpServletRequest
     * @param resp - HttpServletResponse
     * @throws IOException
     */
    @GetMapping(value = "/download")
    public void downLoadPicture(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("pic");
        resp.setContentType("image/png");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
        File file = new File(name);
        try (FileInputStream in = new FileInputStream(file)) {
            resp.getOutputStream().write(in.readAllBytes());
        }
    }

    /**
     * uploads new pictures.
     *
     * @param pic - the picture parameter.
     * @return the json response with the uploaded picture full path.
     * @throws IOException
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    public @ResponseBody
    String uploadPicture(@RequestParam MultipartFile pic) throws IOException {
        String newFilePath = "";
        File folder = new File(REPOSITORY + "/images");
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (pic != null) {
            String filePath = folder + File.separator + pic.getOriginalFilename();
            int i = 0;
            newFilePath = filePath;
            File file = new File(newFilePath);
            while (file.exists()) {
                i++;
                newFilePath = folder + File.separator + i + "_" + pic.getOriginalFilename();
                file = new File(newFilePath);
            }
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(pic.getInputStream().readAllBytes());
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String resultJSON = mapper.writeValueAsString(new JsonResponse(newFilePath));
        return resultJSON;
    }
}
