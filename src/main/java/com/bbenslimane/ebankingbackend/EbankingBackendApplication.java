package com.bbenslimane.ebankingbackend;

import com.bbenslimane.ebankingbackend.entities.*;
import com.bbenslimane.ebankingbackend.enumes.AccountStatus;
import com.bbenslimane.ebankingbackend.enumes.OperationType;
import com.bbenslimane.ebankingbackend.repositories.AccountOperationRepository;
import com.bbenslimane.ebankingbackend.repositories.BankAccountRepository;
import com.bbenslimane.ebankingbackend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BankAccountRepository bankAccountRepository,
                            CustomerRepository customerRepository,
                            AccountOperationRepository accountOperationRepository){
        return  args -> {
            Stream.of("bilal", "kamal", "reda").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*9000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setOverDraft(9000);
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);

                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*9000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setInterestRate(5.5);
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);

                bankAccountRepository.save(savingAccount);
            });

            bankAccountRepository.findAll().forEach(acc->{
                for (int i = 0; i <10 ; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.CREDIT : OperationType.DEBIT);
                    accountOperation.setBankAccount(acc);

                    accountOperationRepository.save(accountOperation);

                }
            });

        };

    }

}
