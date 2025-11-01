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
import java.util.UUID;
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
            String contractId = req.getParameter("contractId");
            String contractName = req.getParameter("contractName");
            String transactionId = req.getParameter("transactionId");

            List<TransactionResponse> response;

            if (transactionId != null && !transactionId.trim().isEmpty()) {
                response = getTransactionById(transactionId);
                if (response.isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JsonParser.writeResponseBody(new ErrorResponse("Transaction not found"), resp);
                    return;
                }
            } else if (contractId != null && !contractId.trim().isEmpty()) {
                response = getTransactionsByContractId(contractId);
            } else if (contractName != null && !contractName.trim().isEmpty()) {
                response = getTransactionsByContractName(contractName);
            } else {
                response = getAllTransactions();
            }

            JsonParser.writeResponseBody(response, resp);

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonParser.writeResponseBody(new ErrorResponse("Internal server error"), resp);
        }
    }

    private List<TransactionResponse> getTransactionById(String transactionId) {
        try {
            Transaction transaction = transactionService.getTransactionById(UUID.fromString(transactionId));
            if (transaction != null) {
                return List.of(TransactionResponse.fromTransaction(transaction));
            }
            return List.of();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction ID format");
        }
    }

    private List<TransactionResponse> getTransactionsByContractId(String contractId) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByContractId(UUID.fromString(contractId));
            return transactions.stream()
                    .map(TransactionResponse::fromTransaction)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid contract ID format");
        }
    }

    private List<TransactionResponse> getTransactionsByContractName(String contractName) {
        List<Transaction> transactions = transactionService.getTransactionsByContractName(contractName);
        return transactions.stream()
                .map(TransactionResponse::fromTransaction)
                .collect(Collectors.toList());
    }

    private List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return transactions.stream()
                .map(TransactionResponse::fromTransaction)
                .collect(Collectors.toList());
    }
}