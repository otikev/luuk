package com.elmenture.core.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Table(name = "tag_properties")
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TagProperty extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public TagProperty(Tag _tag, String value) {
        this.tag = _tag;
        this.value = value;
    }
}
