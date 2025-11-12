package ru.kpfu.itis.servlets;

import ru.kpfu.itis.dto.request.TransactionRequest;
import ru.kpfu.itis.dto.response.ErrorResponse;
import ru.kpfu.itis.dto.response.TransactionResponse;
import ru.kpfu.itis.model.Transaction;
import ru.kpfu.itis.service.TransactionService;
import ru.kpfu.itis.util.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/api/transactions/create")
public class TransactionCreateServlet extends HttpServlet {

    private TransactionService transactionService;

    @Override
    public void init() throws ServletException {
        transactionService = (TransactionService) getServletContext().getAttribute("transactionService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            TransactionRequest request = JsonParser.readRequestBody(req, TransactionRequest.class);

            Transaction transaction = transactionService.create(
                    UUID.fromString(request.getSourceContractId()),
                    UUID.fromString(request.getTargetContractId()),
                    request.getAmount(),
                    request.getDescription()
            );

            TransactionResponse response = TransactionResponse.fromTransaction(transaction);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            JsonParser.writeResponseBody(response, resp);

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonParser.writeResponseBody(new ErrorResponse(e.getMessage()), resp);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonParser.writeResponseBody(new ErrorResponse("Failed to create transaction"), resp);
        }
    }
}
