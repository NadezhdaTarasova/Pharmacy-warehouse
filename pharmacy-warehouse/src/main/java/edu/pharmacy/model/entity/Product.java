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
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "active_substance_name")
    private String activeSubstanceName;
    @Column(name = "price")
    private double price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderProduct> ordersProducts;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<WarehouseProduct> warehousesProducts;
}
