package com.elmenture.core.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by otikev on 17-Mar-2022
 */

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "items")
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "description", nullable = false)
    private String description;

    //XXS XS S M L XL XXL 3XL 4XL 5XL 6XL 7XL
    @Column(name = "size_international")
    private String sizeInternational;

    /**
     * The number corresponding to the size_type
     */
    @Column(name = "size_number")
    private Long sizeNumber;

    /**
     * One of US, UK, EU
     */
    @Column(name = "size_type")
    private String sizeType;

    @Column(name = "price")
    private long price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "target") //m or f or c
    private String target;

    @Column(name = "sold")
    private Boolean sold;

    //@OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //private List<ItemProperty> itemProperties;

    public Item(String description, String sizeInternational, String sizeType, Long sizeNumber, long price, String imageUrl) {
        this.description = description;
        this.sizeType = sizeType;
        this.sizeInternational = sizeInternational;
        this.sizeNumber = sizeNumber;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}