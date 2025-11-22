package ru.kpfu.itis.servlets;

import ru.kpfu.itis.dto.request.TransactionRequest;
import ru.kpfu.itis.dto.response.ApiResponse;
import ru.kpfu.itis.dto.response.TransactionResponse;
import ru.kpfu.itis.model.Transaction;
import ru.kpfu.itis.service.TransactionService;
import ru.kpfu.itis.util.JsonParser;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

@WebServlet("/api/transactions/create")
public class TransactionCreateServlet extends HttpServlet {
    private TransactionService transactionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        transactionService = (TransactionService) config.getServletContext().getAttribute("transactionService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            TransactionRequest request = JsonParser.readRequestBody(req, TransactionRequest.class);

            if (request == null ||
                    request.getSourceContractId() == null ||
                    request.getTargetContractId() == null ||
                    request.getAmount() == null ||
                    request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ApiResponse<Object> errorResponse = new ApiResponse<>("Invalid request data", null);
                JsonParser.writeResponseBody(errorResponse, resp);
                return;
            }

            try {
                UUID.fromString(request.getSourceContractId());
                UUID.fromString(request.getTargetContractId());
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ApiResponse<Object> errorResponse = new ApiResponse<>("Invalid UUID format", null);
                JsonParser.writeResponseBody(errorResponse, resp);
                return;
            }

            Transaction transaction = transactionService.create(
                    UUID.fromString(request.getSourceContractId()),
                    UUID.fromString(request.getTargetContractId()),
                    request.getAmount(),
                    request.getDescription()
            );

            TransactionResponse response = TransactionResponse.fromTransaction(transaction);
            ApiResponse<TransactionResponse> apiResponse = new ApiResponse<>("success", response);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            JsonParser.writeResponseBody(apiResponse, resp);

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ApiResponse<Object> errorResponse = new ApiResponse<>(e.getMessage(), null);
            JsonParser.writeResponseBody(errorResponse, resp);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<Object> errorResponse = new ApiResponse<>("Failed to create transaction", null);
            JsonParser.writeResponseBody(errorResponse, resp);
        }
    }
}