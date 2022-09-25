package com.bbenslimane.ebankingbackend.services;

import com.bbenslimane.ebankingbackend.entities.*;
import com.bbenslimane.ebankingbackend.enumes.OperationType;
import com.bbenslimane.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.bbenslimane.ebankingbackend.exceptions.BankAccountNotFound;
import com.bbenslimane.ebankingbackend.exceptions.CustomerNotFoundException;
import com.bbenslimane.ebankingbackend.repositories.AccountOperationRepository;
import com.bbenslimane.ebankingbackend.repositories.BankAccountRepository;
import com.bbenslimane.ebankingbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private AccountOperationRepository accountOperationRepository;
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    //Logger log = LoggerFactory.getLogger(this.getClass().getName()); // or we use lombock with the annotation @Slf4j

    //we can use Lombock to create this constructor, for the dependency injection
//    public BankAccountServiceImpl(AccountOperationRepository accountOperationRepository,
//                                  BankAccountRepository bankAccountRepository,
//                                  CustomerRepository customerRepository)
//    {
//        this.accountOperationRepository = accountOperationRepository;
//        this.bankAccountRepository = bankAccountRepository;
//        this.customerRepository = customerRepository;
//    }


    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("saving new customer");
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if(customer == null ) throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);

        CurrentAccount savedAccount =  bankAccountRepository.save(currentAccount);

        return savedAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if(customer == null ) throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);

        SavingAccount savedAccount =  bankAccountRepository.save(savingAccount);

        return savedAccount;

    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFound {
        BankAccount bankAccount =  bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFound("Bank Account Not Found "));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFound, BalanceNotSufficientException {

        BankAccount bankAccount = getBankAccount(accountId);

        if(bankAccount.getBalance() < amount) throw new BalanceNotSufficientException("Balance Not Sufficient");

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFound {
        BankAccount bankAccount = getBankAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFound, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from "+accountIdSource);
    }

    @Override
    public List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    }
}
