package ru.kpfu.itis.listeners;

import ru.kpfu.itis.config.DataBaseConfig;
import ru.kpfu.itis.repository.ContractRepository;
import ru.kpfu.itis.repository.TransactionRepository;
import ru.kpfu.itis.repository.impl.ContractRepositoryImpl;
import ru.kpfu.itis.repository.impl.TransactionRepositoryImpl;
import ru.kpfu.itis.service.TransactionService;
import ru.kpfu.itis.service.impl.TransactionServiceImpl;

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

        TransactionRepository transactionRepository = new TransactionRepositoryImpl(DataBaseConfig.jdbcTemplate);

        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);

        context.setAttribute("transactionService", transactionService);
        context.setAttribute("transactionRepository", transactionRepository);
    }
}
