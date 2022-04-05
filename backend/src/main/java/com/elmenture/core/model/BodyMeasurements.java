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
@Table(name = "body_measurements")
public class BodyMeasurements extends BaseEntity {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "size_international")
    private String sizeInternational;
    @Column(name = "size_us")
    private int sizeUs;
    @Column(name = "size_uk")
    private int sizeUk;
    @Column(name = "size_eu")
    private int sizeEu;
    @Column(name = "chest")
    private int chest;
    @Column(name = "waist")
    private int waist;
    @Column(name = "hips")
    private int hips;
}