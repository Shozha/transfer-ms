package ru.kpfu.itis.servlets;

import ru.kpfu.itis.repository.ContractRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import static ru.kpfu.itis.util.JsonParser.writeResponseBody;

@WebServlet("/transfer-balance")
public class GetBalanceServlet extends HttpServlet {

    private ContractRepository contractRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contractName = req.getParameter("contractName");

        BigDecimal balance = contractRepository.getBalanceByContractName(contractName);

        Map<String, Object> response = Map.of(
                "contractName", contractName,
                "balance", balance
        );

        writeResponseBody(response, resp);

        String operation = resp.getHeader("operation-id");
        resp.addHeader(operation, "success");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        contractRepository = (ContractRepository) getServletContext().getAttribute("contractRepository");
    }
}
