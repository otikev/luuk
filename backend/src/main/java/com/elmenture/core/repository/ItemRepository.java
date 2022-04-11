package com.elmenture.core.repository;

import com.elmenture.core.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by otikev on 06-Mar-2022
 */

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByTargetIn(List<String> targets);
    Page<Item> findByTargetIn(List<String> targets, Pageable pageable);
    List<Item> findByTargetInAndSizeInternationalIs(List<String> targets, String sizeInternational);
    Page<Item> findByTargetInAndSizeInternationalIs(List<String> targets, String sizeInternational, Pageable pageable);
}
