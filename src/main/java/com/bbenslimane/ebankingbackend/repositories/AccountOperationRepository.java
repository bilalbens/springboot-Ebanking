package com.bbenslimane.ebankingbackend.repositories;

import com.bbenslimane.ebankingbackend.entities.AccountOperation;
import com.bbenslimane.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}