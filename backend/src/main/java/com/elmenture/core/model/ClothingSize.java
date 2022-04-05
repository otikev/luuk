package com.elmenture.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by otikev on 06-Mar-2022
 */

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "clothing_size")
public class ClothingSize extends BaseEntity {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "international")
    private String international;
    @Column(name = "us")
    private int us;
    @Column(name = "uk")
    private int uk;
    @Column(name = "eu")
    private int eu;
}