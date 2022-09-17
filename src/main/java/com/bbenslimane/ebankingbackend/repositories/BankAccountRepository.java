package com.bbenslimane.ebankingbackend.repositories;

import com.bbenslimane.ebankingbackend.entities.BankAccount;
import com.bbenslimane.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}