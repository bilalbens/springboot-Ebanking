package com.bbenslimane.ebankingbackend.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CA") // to add "CA" to the type column in the bankAccount table
@Data @AllArgsConstructor @NoArgsConstructor
public class CurrentAccount extends BankAccount{

    private double overDraft;
}
