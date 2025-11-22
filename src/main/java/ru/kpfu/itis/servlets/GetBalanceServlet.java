
package ru.kpfu.itis.servlets;

import ru.kpfu.itis.dto.response.ApiResponse;
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

@WebServlet("/api/transactions/balance")
public class GetBalanceServlet extends HttpServlet {

    private ContractRepository contractRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String contractName = req.getParameter("contractName");

            if (contractName == null || contractName.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ApiResponse<Object> errorResponse = new ApiResponse<>("contractName parameter is required", null);
                writeResponseBody(errorResponse, resp);
                return;
            }

            BigDecimal balance = contractRepository.getBalanceByContractName(contractName);

            if (balance == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                ApiResponse<Object> errorResponse = new ApiResponse<>("contract not found", null);
                writeResponseBody(errorResponse, resp);
                return;
            }

            Map<String, Object> data = Map.of(
                    "contractName", contractName,
                    "balance", balance
            );

            ApiResponse<Map<String, Object>> response = new ApiResponse<>("success", data);
            writeResponseBody(response, resp);

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<Object> errorResponse = new ApiResponse<>("Internal server error", null);
            writeResponseBody(errorResponse, resp);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        contractRepository = (ContractRepository) config.getServletContext().getAttribute("contractRepository");
    }
}
