package com.epam.esm.config;

import com.epam.esm.dao.config.DAOConfig;
import com.epam.esm.service.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@PropertySource("classpath:application.properties")
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final String ACTIVE_PROFILE_PROD = "prod";

    @Value("${profile.active}")
    private String profile;

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { ServiceConfig.class, DAOConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (profile == null){
            profile = ACTIVE_PROFILE_PROD;
        }
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
        super.onStartup(servletContext);
    }
}
