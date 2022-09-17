package com.bbenslimane.ebankingbackend.entities;

import com.bbenslimane.ebankingbackend.enumes.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // when we have the inheritance we need to specify the strategy (single table - table per class - joined table)
@DiscriminatorColumn(name = "TYPE", length = 4) // in the single table  strategy, we should add a new type of the inheritance class (currentAcount & SavingAccount)
@Data @AllArgsConstructor @NoArgsConstructor
public class BankAccount {
    @Id
    private String id;
    private double balance;
    private Date createdAt;
    //@Enumerated(EnumType.ORDINAL) // to save it in the db as a number 0:CREATED, 1:ACTIVATED...
    @Enumerated(EnumType.STRING) // to save it as a string
    private AccountStatus status;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.EAGER) // it's better to use LAZY to not load a lot of data in memory
    private List<AccountOperation> accountOperations;
}
