package com.elmenture.core.repository;

import com.elmenture.core.model.TagProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by otikev on 06-Mar-2022
 */

public interface TagPropertyRepository extends JpaRepository<TagProperty, Long> {
    @Query("SELECT t FROM TagProperty t WHERE lower(t.value) LIKE lower(concat('%', :nameToFind,'%'))")
    List<TagProperty> searchAllLike(@Param("nameToFind") String keyword);

    List<TagProperty> findByTagId(Long tagId);

    TagProperty findByValue(String value);

    List<TagProperty> findByValueAndTagId(String value, Long tagId);

    @Query(value = "select t.id from TagProperty t where t.value in :values")
    List<Long> getTagPropertyIds(@Param("values") List<String> recommendation);

    @Query(value = "select t.id from TagProperty t where t.value like :values")
    List<Long> getTagPropertyIds(@Param("values") String value);
}
