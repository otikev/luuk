package com.elmenture.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by otikev on 16-Mar-2022
 */

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "item_properties")
@NoArgsConstructor
public class ItemProperty extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_property_id", nullable = false)
    private TagProperty tagProperty;

    public ItemProperty(Item item, TagProperty tagProperty) {
        this.item = item;
        this.tagProperty = tagProperty;
    }
}
