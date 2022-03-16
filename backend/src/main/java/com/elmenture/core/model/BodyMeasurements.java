package com.elmenture.core.model;

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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "neck", nullable = true)
    private int neck;

    @Column(name = "shoulder", nullable = true)
    private int shoulder;

    @Column(name = "chest", nullable = true)
    private int chest;

    @Column(name = "waist", nullable = true)
    private int waist;

    @Column(name = "thigh", nullable = true)
    private int thigh;

    @Column(name = "leg", nullable = true)
    private int leg;
}