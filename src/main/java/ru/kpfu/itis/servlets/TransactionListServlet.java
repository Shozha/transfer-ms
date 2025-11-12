package ru.kpfu.itis.servlets;

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
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/transactions")
public class TransactionListServlet extends HttpServlet {

    private TransactionService transactionService;

    @Override
    public void init() throws ServletException {
        transactionService = (TransactionService) getServletContext().getAttribute("transactionService");
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

            JsonParser.writeResponseBody(response, resp);

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonParser.writeResponseBody(new ErrorResponse(e.getMessage()), resp);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonParser.writeResponseBody(new ErrorResponse("Internal server error"), resp);
        }
    }
}
