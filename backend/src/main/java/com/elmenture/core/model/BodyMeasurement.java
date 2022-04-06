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
@Table(name = "body_measurement")
public class BodyMeasurement extends BaseEntity {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "chest_cm")
    private Integer chest_cm;
    @Column(name = "waist_cm")
    private Integer waist_cm;
    @Column(name = "hips_cm")
    private Integer hips_cm;
}