package ru.job4j.web;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.domain.Car;
import ru.job4j.repository.UserRepository;
import ru.job4j.service.ServiceInterface;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TableViewController.class)
public class TableViewControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ServiceInterface service;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void whenShowStartingPageThenTablePage() throws Exception {
        Car car1 = new Car();
        Car car2 = new Car();
        ArrayList<Car> list = new ArrayList<>(Lists.newArrayList(car1, car2));
        given(this.service.loadTable())
                .willReturn(list);

        given(this.service.allBrands())
                .willReturn(new ArrayList<>(
                        Lists.newArrayList("Ford")));

        this.mvc.perform(get("/").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(model().attribute("list", hasSize(2)))
                .andExpect(model().attribute("brands", hasItems("Ford")))
                .andExpect(model().attribute("title", is("Log In")))
                .andExpect(view().name("table"));
    }

    @Test
    public void whenFilterThenTablePage() throws Exception {
        boolean day = false;
        boolean photo = false;
        String brand = "Ford";
        StringBuilder url = new StringBuilder();
        url.append("/filter?day=")
                .append(day)
                .append("&photo=")
                .append(photo)
                .append("&brand=")
                .append(brand);
        Car car1 = new Car();
        Car car2 = new Car();
        ArrayList<Car> list = new ArrayList<>(Lists.newArrayList(car1, car2));
        given(this.service.filter(day, photo, brand))
                .willReturn(list);
        given(this.service.allBrands())
                .willReturn(new ArrayList<>(
                        Lists.newArrayList("Ford")));
        this.mvc.perform(get(url.toString()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(model().attribute("list", hasSize(2)))
                .andExpect(model().attribute("brands", hasItems("Ford")))
                .andExpect(model().attribute("title", is("Log In")))
                .andExpect(view().name("table"));
    }
}