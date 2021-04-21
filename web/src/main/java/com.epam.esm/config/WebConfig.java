package com.epam.esm.config;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.ExceptionHandlerController;
import com.epam.esm.controller.TagController;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.epam.esm.service.config"})
@Profile({"prod", "dev"})
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public TagController tagController(TagService tagService) {
        return new TagController(tagService);
    }

    @Bean
    public CertificateController certificateController(GiftCertificateService giftCertificateService) {
        return new CertificateController(giftCertificateService);
    }

    @Bean
    public ExceptionHandlerController exceptionHandlerController() {
        return new ExceptionHandlerController();
    }
}
