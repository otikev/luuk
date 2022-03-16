package com.elmenture.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by otikev on 17-Mar-2022
 */

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "items")
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description", nullable = false)
    private String description;

    //XXS	XS	S	M	L	XL	XXL	3XL	4XL	5XL	6XL	7XL
    @Column(name = "size_international")
    private String sizeInternational;

    @Column(name = "size_number")
    private long sizeNumber;

    @Column(name = "price")
    private long price;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<ItemProperty> itemProperties;
}