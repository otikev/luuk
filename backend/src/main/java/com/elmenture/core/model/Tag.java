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
@Table(name = "tags")
@NoArgsConstructor
public class Tag extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "value", nullable = false)
    private String value;

    public Tag(String value) {
        this.value = value;
    }

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<TagProperty> tagProperties;
}
