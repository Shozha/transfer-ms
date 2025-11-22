package ru.kpfu.itis.servlets;

import ru.kpfu.itis.dto.response.ApiResponse;
import ru.kpfu.itis.dto.response.ContractResponse;
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
        try {
            Map<String, Object> requestBody = JsonParser.readRequestBody(req, Map.class);
            String contractName = (String) requestBody.get("contractName");

            if (contractName == null || contractName.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ApiResponse<Object> errorResponse = new ApiResponse<>("contractName is required", null);
                JsonParser.writeResponseBody(errorResponse, resp);
                return;
            }

            Contract contract = Contract.builder()
                    .contractName(contractName)
                    .createdDate(Instant.now())
                    .balance(BigDecimal.ZERO)
                    .build();

            Contract savedContract = contractRepository.save(contract);
            ContractResponse response = ContractResponse.fromContract(savedContract);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            ApiResponse<ContractResponse> apiResponse = new ApiResponse<>("success", response);
            JsonParser.writeResponseBody(apiResponse, resp);

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<Object> errorResponse = new ApiResponse<>("Failed to create contract", null);
            JsonParser.writeResponseBody(errorResponse, resp);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        contractRepository = (ContractRepository) config.getServletContext().getAttribute("contractRepository");
    }
}
