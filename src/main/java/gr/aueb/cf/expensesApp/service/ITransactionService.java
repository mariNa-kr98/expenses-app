package gr.aueb.cf.expensesApp.service;

import gr.aueb.cf.expensesApp.core.exceptions.AppException;
import gr.aueb.cf.expensesApp.model.Transaction;

public interface ITransactionService {

    Transaction saveTransaction(Transaction transaction) throws AppException;
    Transaction modifyTransaction(Long transactionId, Transaction transaction) throws AppException;
    Transaction deleteTransaction(Long transactionId) throws AppException;
}
