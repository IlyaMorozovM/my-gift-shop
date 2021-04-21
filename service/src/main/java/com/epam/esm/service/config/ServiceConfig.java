package com.epam.esm.service.config;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDao;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.service.util.CertificateValidator;
import com.epam.esm.service.util.TagValidator;
import com.epam.esm.service.util.impl.CertificateValidatorImpl;
import com.epam.esm.service.util.impl.TagValidatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(basePackages = {"com.epam.esm.dao.config"})
@Profile({"prod", "dev"})
public class ServiceConfig {

    @Bean
    public TagValidator tagValidator() {
        return new TagValidatorImpl();
    }

    @Bean
    public TagServiceImpl tagServiceImpl(TagDao tagDao, TagValidator tagValidator) {
        return new TagServiceImpl(tagDao, tagValidator);
    }

    @Bean
    public CertificateValidator certificateValidator() {
        return new CertificateValidatorImpl();
    }

    @Bean
    public GiftCertificateServiceImpl giftCertificateService(
            GiftCertificateDAO giftCertificateDAO, TagDao tagDao, CertificateValidator certificateValidator) {
        return new GiftCertificateServiceImpl(giftCertificateDAO, tagDao, certificateValidator);
    }
}
