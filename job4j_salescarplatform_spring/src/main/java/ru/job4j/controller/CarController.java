package ru.job4j.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import ru.job4j.models.Car;
import ru.job4j.models.JsonResponse;
import ru.job4j.models.User;
import ru.job4j.service.ServiceInterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Component
public class CarController {

    @Autowired
    public CarController(final ServiceInterface service) {
        this.service = service;
    }

    private final ServiceInterface service;

    private static final Logger LOG = LogManager.getLogger(CarController.class.getName());

    private static final String REPOSITORY = System.getProperty("java.io.tmpdir");

    ///ADDCAR servolet get addingPage.jsp
    @GetMapping(value = "/add")
    public String addingPage() {
        return "addingPage";
    }

    //Add car servlet post
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public @ResponseBody String addCar(@RequestBody String jsonString, HttpSession session) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> carFields = objectMapper.readValue(jsonString, Map.class);
        Car car = new Car(carFields);
        int userId = (Integer) session.getAttribute("id");
        car.setUser(new User(userId));
        boolean result = this.service.addCar(car);
        String status = "invalid";
        if (result) {
            status = "valid";
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String resultJSON = ow.writeValueAsString(new JsonResponse(status));
        return resultJSON;
    }

    //Change status
    @PostMapping(value = "/change", consumes = "application/json", produces = "application/json")
    public @ResponseBody String changeStatus(@RequestBody String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> carFields = objectMapper.readValue(jsonString, Map.class);
        Car car = new Car(carFields);
        boolean result = this.service.changeStatus(car);
        String status = "invalid";
        if (result) {
            status = "valid";
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String resultJSON = ow.writeValueAsString(new JsonResponse(status));
        return resultJSON;
    }

    //down load servlet
    @GetMapping(value = "/download")
    public void downLoadPicture(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("pic");
        resp.setContentType("name=" + name);
        resp.setContentType("image/png");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
        File file = new File(name);
        try (FileInputStream in = new FileInputStream(file)) {
            resp.getOutputStream().write(in.readAllBytes());
        }
    }

    //upload servlet
    @PostMapping(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    public @ResponseBody String uploadPicture(@RequestParam CommonsMultipartFile pic) throws IOException {
        String newFilePath = "";
        File folder = new File(REPOSITORY + "/images");
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (pic != null) {
            FileItem picture = pic.getFileItem();
            String filePath = folder + File.separator + picture.getName();
            int i = 0;
            newFilePath = filePath;
            File file = new File(newFilePath);
            while (file.exists()) {
                i++;
                newFilePath = folder + File.separator + i + "_" + picture.getName();
                file = new File(newFilePath);
            }
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(picture.getInputStream().readAllBytes());
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String resultJSON = ow.writeValueAsString(new JsonResponse(newFilePath));
        return resultJSON;
    }



}
