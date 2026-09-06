package ru.itis.configuration;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class ApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(DataBaseConfig.class);
        applicationContext.register(WebConfig.class);

        ContextLoaderListener contextLoaderListener = new ContextLoaderListener(applicationContext);
        servletContext.addListener(contextLoaderListener);

        ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet("dispatcher",
                new DispatcherServlet(applicationContext));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/");
    }
}
