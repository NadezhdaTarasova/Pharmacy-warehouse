package edu.pharmacy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private String iban;
    @Column(name = "balance")
    private double balance;
    @OneToOne(mappedBy = "account")
    private Pharmacy pharmacy;
    @OneToOne(mappedBy = "account")
    private Supplier supplier;
}
