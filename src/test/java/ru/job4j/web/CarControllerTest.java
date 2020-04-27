package ru.job4j.web;

import com.google.common.io.Files;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.job4j.domain.Car;
import ru.job4j.domain.JsonResponse;
import ru.job4j.repository.UserRepository;
import ru.job4j.service.ServiceInterface;

import java.io.File;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ServiceInterface service;

    @MockBean
    private UserRepository userRepository;

    private static final String PIC_PATH = "./src/test/java/ru/job4j/web/testPicture.jpg";
    private static final String PIC_NAME = "testPicture.jpg";
    private static final String REPOSITORY = System.getProperty("java.io.tmpdir");

    @Test
    @WithMockUser(username = "User", password = "user")
    public void whenOpenAddPageThenAddPage() throws Exception {
        this.mvc.perform(get("/add").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("addingPage"));
    }

    @Test
    @WithMockUser(username = "User", password = "user")
    public void whenAddCarThenJSON() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", 10);
        ObjectMapper mapper = new ObjectMapper();
        CarForTest carForTest = new CarForTest();
        carForTest.setType("Sedan");
        carForTest.setBrand("Lada");
        carForTest.setModel("Priora");
        carForTest.setUsage("10000");
        carForTest.setYear("2019");
        carForTest.setDesc("Very good car");
        carForTest.setPrice("300000");
        carForTest.setPicPath("");
        String requestJson = mapper.writeValueAsString(carForTest);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).accept(MediaType.APPLICATION_JSON)
                .session(session);
        this.mvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(new JsonResponse("valid"))));
    }

    @Test
    @WithMockUser(username = "User", password = "user")
    public void whenChangeStatusThenJSON() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StatusChanging newStatus = new StatusChanging(10, true);
        String requestJson = mapper.writeValueAsString(newStatus);
        Car car = new Car();
        car.setId(newStatus.getId());
        car.setSold(newStatus.getStatus());
        given(this.service.changeStatus(car))
                .willReturn(true);
        this.mvc.perform(post("/change")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(new JsonResponse("valid"))));
    }

    @Test
    @WithMockUser(username = "User", password = "user")
    public void whenDownloadThenByteArray() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/download")
                .contentType(MediaType.TEXT_HTML)
                .param("pic", PIC_PATH);
        MvcResult mvcResult = this.mvc.perform(builder)
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(25141, mvcResult.getResponse().getContentAsByteArray().length);
        assertEquals("image/png", mvcResult.getResponse().getContentType());
    }

    @Test
    @WithMockUser(username = "User", password = "user")
    public void whenUploadThenJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File picture = new File(PIC_PATH);
        byte[] byteArray = Files.toByteArray(picture);
        Path path = Path.of(REPOSITORY, "images", PIC_NAME);
        String filePath = REPOSITORY + "images" + File.separator + PIC_NAME;
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "pic", PIC_NAME,
                "multipart/form-data", byteArray);
        this.mvc.perform(MockMvcRequestBuilders
                .multipart("/upload")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(new JsonResponse(filePath))));
        path.toFile().delete();
    }


    private static class CarForTest {

        private String type;
        private String brand;
        private String model;
        private String usage;
        private String year;
        private String desc;
        private String price;
        private String picPath;

        public CarForTest() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getUsage() {
            return usage;
        }

        public void setUsage(String usage) {
            this.usage = usage;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPicPath() {
            return picPath;
        }

        public void setPicPath(String picPath) {
            this.picPath = picPath;
        }
    }

    private static class StatusChanging {
        private int id;
        private boolean status;

        public StatusChanging() {
        }

        public StatusChanging(int id, boolean status) {
            this.id = id;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }

}