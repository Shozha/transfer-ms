package ru.kpfu.itis.listeners;

import ru.kpfu.itis.config.DataBaseConfig;
import ru.kpfu.itis.repository.ContractRepository;
import ru.kpfu.itis.repository.impl.ContractRepositoryImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ProjectStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        ContractRepository contractRepository = new ContractRepositoryImpl(DataBaseConfig.jdbcTemplate);
        context.setAttribute("contractRepository", contractRepository);
    }
}
