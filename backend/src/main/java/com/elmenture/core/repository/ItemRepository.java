package com.elmenture.core.repository;

import com.elmenture.core.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by otikev on 06-Mar-2022
 */

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByTarget(String target);

    List<Item> findByTargetIn(List<String> targets);

    Page<Item> findBySoldAndIdGreaterThan(boolean sold, long id, Pageable pageable);

    Page<Item> findByTargetIn(List<String> targets, Pageable pageable);

    Page<Item> findByTargetInAndSoldFalse(List<String> targets, Pageable pageable);

    Page<Item> findByTargetInAndSoldTrue(List<String> targets, Pageable pageable);

    List<Item> findBySoldNull();

    List<Item> findByTargetInAndSizeInternationalIs(List<String> targets, String sizeInternational);

    //@Query("select a from Item a where sold = true and id in :ids")
    List<Item> findBySoldAndIdIn(boolean sold, List<Long> ids);

    @Query(value = "select sum(price) from Item where id in :ids")
    int sumOfPriceIn(@Param("ids") List<Long> ids);

    Page<Item> findByTargetInAndSizeInternationalIsAndSoldIs(List<String> targets, String sizeInternational, boolean sold, Pageable pageable);

    Page<Item> findByTargetInAndSizeInternationalIs(List<String> targets, String sizeInternational, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.sold =:is_sold AND lower(i.description) LIKE lower(concat('%', :nameToFind,'%')) OR lower(i.brand) LIKE lower(concat('%', :nameToFind,'%'))")
    List<Item> searchAllLike(@Param("nameToFind") String keyword, @Param("is_sold") boolean sold);

    @Query(value = "select * from items where size_type =?1 and size_number=?3 or size_international=?2 order by price ASC limit ?4 ", nativeQuery = true)
    List<Item> fetchAllBySize(String sizeType, String sizeInternational, int sizeNumber, long limit);

    @Query(value = "select count(*) from items where size_type =?1 and size_number=?3 or size_international=?2", nativeQuery = true)
    Long countOfUserSizedItems(String sizeType, String sizeInternational, int sizeNumber);

    List<Item> findByBrandIn(List<String> brandNames);
}
