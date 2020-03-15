package ru.job4j.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import ru.job4j.logic.Service;
import ru.job4j.logic.ServiceInterface;
import ru.job4j.models.Car;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * the servlet for changing the status of the offer.
 */
public class ChangeStatusServlet extends HttpServlet {

    private ServiceInterface service = Service.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        reader.lines().forEach(sb::append);
        ObjectMapper mapper = new ObjectMapper();
        String json = sb.toString();
        HashMap map = mapper.readValue(json, HashMap.class);
        int id = (Integer) map.get("id");
        boolean sold = (Boolean) map.get("status");
        Car car = new Car();
        car.setId(id);
        car.setSold(sold);
        boolean result  = this.service.changeStatus(car);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        JSONObject status = new JSONObject();
        if (result) {
            status.put("status", "valid");
        } else {
            status.put("status", "invalid");
        }
        writer.append(status.toString());
        writer.flush();
    }
}
