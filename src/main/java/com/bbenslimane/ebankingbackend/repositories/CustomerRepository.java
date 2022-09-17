package com.bbenslimane.ebankingbackend.repositories;

import com.bbenslimane.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}