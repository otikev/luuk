package com.elmenture.core.controller;

import com.elmenture.core.domain.BodyMeasurementsRequest;
import com.elmenture.core.model.BodyMeasurements;
import com.elmenture.core.model.User;
import com.elmenture.core.repository.BodyMeasurementsRepository;
import com.elmenture.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by otikev on 06-Mar-2022
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BodyMeasurementsRepository bodyMeasurementsRepository;

    @PostMapping(value = "measurements/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postBodyMeasurements(@RequestHeader HttpHeaders headers, @Valid @RequestBody BodyMeasurements request) {
        String token = headers.get("luuk-x-authorization").get(0);
        User user = userRepository.findByAuthToken(token);
        if (user == null) {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }

        BodyMeasurements measurements = user.getBodyMeasurements();
        if (measurements == null) {
            measurements = new BodyMeasurements();
        }
        measurements.setSizeInternational(request.getSizeInternational());
        measurements.setSizeUs(request.getSizeUs());
        measurements.setSizeUk(request.getSizeUk());
        measurements.setSizeEu(request.getSizeEu());
        measurements.setChest(request.getChest());
        measurements.setWaist(request.getWaist());
        measurements.setHips(request.getHips());

        user.setBodyMeasurements(measurements);
        userRepository.save(user);

        return ResponseEntity.ok("Updated Successfully");
    }
}
