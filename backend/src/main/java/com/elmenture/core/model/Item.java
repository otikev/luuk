package com.elmenture.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by otikev on 17-Mar-2022
 */

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "items")
@NoArgsConstructor
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
    private Long sizeNumber;

    @Column(name = "price")
    private long price;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<ItemProperty> itemProperties;

    public Item(String description, String sizeInternational, Long sizeNumber, long price, String imageUrl) {
        this.description = description;
        this.sizeInternational = sizeInternational;
        this.sizeNumber = sizeNumber;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}