package com.bbenslimane.ebankingbackend.services;

import com.bbenslimane.ebankingbackend.entities.BankAccount;
import com.bbenslimane.ebankingbackend.entities.CurrentAccount;
import com.bbenslimane.ebankingbackend.entities.Customer;
import com.bbenslimane.ebankingbackend.entities.SavingAccount;
import com.bbenslimane.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.bbenslimane.ebankingbackend.exceptions.BankAccountNotFound;
import com.bbenslimane.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);

    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    List<Customer> listCustomers();

    BankAccount getBankAccount(String accountId) throws BankAccountNotFound;

    void debit(String accountId, double amount, String description) throws BankAccountNotFound, BalanceNotSufficientException;

    void credit(String accountId, double amount, String description) throws BankAccountNotFound;

    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFound, BalanceNotSufficientException;


    List<BankAccount> bankAccountList();
}
