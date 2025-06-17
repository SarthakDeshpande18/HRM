package SRMS.FinalProject.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    private static final String UPLOAD_DIR = "file:D:/Duplicate/HRM (2)/HRM/uploads/";
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/uploads/profile-pictures/**")
//                .addResourceLocations("file:" + UPLOAD_DIR);
//    }
//}


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/Duplicate/HRM (2)/HRM/uploads/");
    }
}
