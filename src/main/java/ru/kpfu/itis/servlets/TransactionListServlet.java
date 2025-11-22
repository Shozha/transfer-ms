package ru.kpfu.itis.servlets;

import ru.kpfu.itis.dto.response.ApiResponse;
import ru.kpfu.itis.dto.response.TransactionResponse;
import ru.kpfu.itis.service.TransactionService;
import ru.kpfu.itis.util.JsonParser;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/transactions")
public class TransactionListServlet extends HttpServlet {

    private TransactionService transactionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        transactionService = (TransactionService) config.getServletContext().getAttribute("transactionService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String contractName = req.getParameter("contractName");

            List<TransactionResponse> response;

            if (contractName != null && !contractName.trim().isEmpty()) {
                response = transactionService.getTransactionsByContractName(contractName)
                        .stream()
                        .map(TransactionResponse::fromTransaction)
                        .collect(Collectors.toList());
            } else {
                response = transactionService.getAllTransactions()
                        .stream()
                        .map(TransactionResponse::fromTransaction)
                        .collect(Collectors.toList());
            }

            ApiResponse<List<TransactionResponse>> apiResponse = new ApiResponse<>("success", response);
            JsonParser.writeResponseBody(apiResponse, resp);

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ApiResponse<Object> errorResponse = new ApiResponse<>(e.getMessage(), null);
            JsonParser.writeResponseBody(errorResponse, resp);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<Object> errorResponse = new ApiResponse<>("Internal server error", null);
            JsonParser.writeResponseBody(errorResponse, resp);
        }
    }
}