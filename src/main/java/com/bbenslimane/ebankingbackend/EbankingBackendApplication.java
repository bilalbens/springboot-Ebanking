package com.bbenslimane.ebankingbackend;

import com.bbenslimane.ebankingbackend.entities.*;
import com.bbenslimane.ebankingbackend.enumes.AccountStatus;
import com.bbenslimane.ebankingbackend.enumes.OperationType;
import com.bbenslimane.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.bbenslimane.ebankingbackend.exceptions.BankAccountNotFound;
import com.bbenslimane.ebankingbackend.exceptions.CustomerNotFoundException;
import com.bbenslimane.ebankingbackend.repositories.AccountOperationRepository;
import com.bbenslimane.ebankingbackend.repositories.BankAccountRepository;
import com.bbenslimane.ebankingbackend.repositories.CustomerRepository;
import com.bbenslimane.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("bilal", "kamal", "rachid").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });

            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*9000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId());

                     List<BankAccount> bankAccountList =  bankAccountService.bankAccountList();

                     for (BankAccount bankAccount:bankAccountList){
                         for (int i = 0; i <10 ; i++) {
                             bankAccountService.credit(bankAccount.getId(), 10000+Math.random()*120000, "Credit");
                             bankAccountService.debit(bankAccount.getId(), 1000+Math.random()*9000, "Debit");

                         }
                     }

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                } catch (BankAccountNotFound | BalanceNotSufficientException e) {
                    e.printStackTrace();
                }
            });
        };
    }

//    @Bean
//    CommandLineRunner commandLineRunner(BankAccountRepository bankAccountRepository){
//        return  args -> {
//            BankAccount bankAccount = bankAccountRepository.findById("041effcf-8298-4fbc-a115-de4286e70c50").orElse(null);
//
//            if(bankAccount != null) {
//                System.out.println("********************************");
//                System.out.println(bankAccount.getId());
//                System.out.println(bankAccount.getBalance());
//                System.out.println(bankAccount.getStatus());
//                System.out.println(bankAccount.getCreatedAt());
//                System.out.println(bankAccount.getCustomer().getName());
//                System.out.println(bankAccount.getClass().getSimpleName());
//                if (bankAccount instanceof CurrentAccount) {
//                    System.out.println("over draft=>" + ((CurrentAccount) bankAccount).getOverDraft());
//                } else if (bankAccount instanceof SavingAccount) {
//                    System.out.println("rate=>" + ((SavingAccount) bankAccount).getInterestRate());
//                }
//                bankAccount.getAccountOperations().forEach(op -> {
//                    System.out.println("============================");
//                    System.out.println(op.getType());
//                    System.out.println(op.getAmount());
//                    System.out.println(op.getOperationDate());
//                });
//            };
//        };
//    }
    //@Bean
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
