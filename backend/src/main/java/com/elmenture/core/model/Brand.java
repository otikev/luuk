package com.elmenture.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by otikev on 16-Mar-2022
 */

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "brands")
@NoArgsConstructor
public class Brand extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "awareness", nullable = true)
    private String awareness;
    @Column(name = "perception", nullable = true)
    private String perception;

    public Brand(String description, String awareness, String perception) {
        this.description = description;
        this.awareness = awareness;
        this.perception = perception;
    }
}
