package com.elmenture.core.repository;

import com.elmenture.core.model.Item;
import com.elmenture.core.model.ItemProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by otikev on 18-Mar-2022
 */

public interface ItemPropertyRepository extends JpaRepository<ItemProperty, Long> {
    List<ItemProperty> findByItemIdAndIdNotIn(Long itemId, List<Long> ids);
    List<ItemProperty> findByItemId(Long itemId);
    List<ItemProperty> findByTagPropertyId(Long tagPropertyId);

    @Query(value = "select item_id from item_properties where tag_property_id=?1", nativeQuery = true)
    List<Long> findItemIdByTagPropertyId(Long tagPropertyId);

}
