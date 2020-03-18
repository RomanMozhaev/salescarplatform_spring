package ru.job4j.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import java.io.File;

public class WebDesc extends AbstractAnnotationConfigDispatcherServletInitializer {


    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringRootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringWebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
//        return new String[]{"*.do"};
    }

//    @Override
//    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
//        File uploadDirectory = new File(System.getProperty("java.io.tmpdir"));
//        MultipartConfigElement multipartConfigElement =
//                new MultipartConfigElement(uploadDirectory.getAbsolutePath());
//        registration.setMultipartConfig(multipartConfigElement);
//    }
}

//maxUploadSizeInMb, maxUploadSizeInMb **  2, maxUploadSizeInMb/2);