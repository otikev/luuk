package com.elmenture.core.repository;

import com.elmenture.core.model.BodyMeasurements;
import com.elmenture.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by otikev on 06-Mar-2022
 */

public interface BodyMeasurementsRepository extends JpaRepository<BodyMeasurements, Long> {
    BodyMeasurements findByUserId(long userId);
}
