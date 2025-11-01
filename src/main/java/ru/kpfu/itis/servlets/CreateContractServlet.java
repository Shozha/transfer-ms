package ru.kpfu.itis.servlets;

import ru.kpfu.itis.model.Contract;
import ru.kpfu.itis.repository.ContractRepository;
import ru.kpfu.itis.util.JsonParser;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@WebServlet("/api/transactions/contract")
public class CreateContractServlet extends HttpServlet {

    private ContractRepository contractRepository;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> requestBody = JsonParser.readRequestBody(req, Map.class);

        String contractName = (String) requestBody.get("contractName");

        Contract contract = Contract.builder()
                .contractName(contractName)
                .createdDate(Instant.now())
                .balance(BigDecimal.ZERO)
                .build();

        Contract savedContract = contractRepository.save(contract);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        JsonParser.writeResponseBody(savedContract, resp);

        String operation = resp.getHeader("operation-id");
        resp.addHeader(operation, "success");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        contractRepository = (ContractRepository) config.getServletContext().getAttribute("contractRepository");
    }
}
