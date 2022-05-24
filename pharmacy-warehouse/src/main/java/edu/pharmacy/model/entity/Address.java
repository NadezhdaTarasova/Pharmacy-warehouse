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
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "street_id")
    private Street street;
    @Column(name = "house")
    private String house;
    @OneToOne(mappedBy = "address")
    private Pharmacy pharmacy;
    @OneToOne(mappedBy = "address")
    private Supplier supplier;
    @OneToOne(mappedBy = "address")
    private Warehouse warehouse;
}
