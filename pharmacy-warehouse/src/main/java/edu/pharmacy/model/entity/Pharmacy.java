package edu.pharmacy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharmacies")
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
    @Column(name = "phone")
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_iban")
    private Account account;
    @OneToOne(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    private Warehouse warehouse;
    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    private List<Employee> staff;
    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    private List<Order> orders;
}
