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

    @GetMapping(value = "/measurements")
    public ResponseEntity<Object> getBodyMeasurements(@RequestHeader HttpHeaders headers) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        User user = userRepository.findByAuthToken(token);
        if(user == null){
            return new ResponseEntity<>("User does not exist",HttpStatus.BAD_REQUEST);
        }
        BodyMeasurements measurements = bodyMeasurementsRepository.findByUserId(user.getId());
        if(measurements==null ){
            return new ResponseEntity<>("measurements do not exist for user",HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(measurements);

    }

        @PostMapping(value = "measurements/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postBodyMeasurements(@RequestHeader HttpHeaders headers,@Valid @RequestBody BodyMeasurementsRequest request) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        User user = userRepository.findByAuthToken(token);
        if(user == null){
            return new ResponseEntity<>("User does not exist",HttpStatus.BAD_REQUEST);
        }

        BodyMeasurements measurements = bodyMeasurementsRepository.findByUserId(user.getId());
        if (measurements == null) {
            measurements = new BodyMeasurements();
        }
        measurements.setUser(user);
        measurements.setSizeInternational(request.getSizeInternational());
        measurements.setSizeNumber(request.getSizeNumber());
        bodyMeasurementsRepository.save(measurements);

        return ResponseEntity.ok("Updated Successfully");
    }
}
