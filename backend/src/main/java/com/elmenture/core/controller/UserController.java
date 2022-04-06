package com.elmenture.core.controller;

import com.elmenture.core.model.BodyMeasurement;
import com.elmenture.core.model.ClothingSize;
import com.elmenture.core.model.User;
import com.elmenture.core.repository.BodyMeasurementsRepository;
import com.elmenture.core.repository.UserRepository;
import com.elmenture.core.service.impl.data.UserMeasurementsDto;
import com.elmenture.core.utils.MiscUtils;
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
    public ResponseEntity<String> postBodyMeasurements(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserMeasurementsDto request) {
        String token = MiscUtils.getUserTokenFromHeader(headers.get("luuk-x-authorization").get(0));
        User user = userRepository.findByAuthToken(token);
        if (user == null) {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }
        if(request.getBodyMeasurements()!=null){
            BodyMeasurement measurements = user.getBodyMeasurement();
            if (measurements == null) {
                measurements = new BodyMeasurement();
            }
            measurements.setChest_cm(request.getBodyMeasurements().getChest());
            measurements.setWaist_cm(request.getBodyMeasurements().getWaist());
            measurements.setHips_cm(request.getBodyMeasurements().getHips());

            user.setBodyMeasurement(measurements);
            userRepository.save(user);
        }else if (request.getClothingSizes()!=null){
            ClothingSize clothingSize = user.getClothingSize();
            if (clothingSize == null) {
                clothingSize = new ClothingSize();
            }
            clothingSize.setInternational(request.getClothingSizes().getInternational());
            clothingSize.setEu(request.getClothingSizes().getEu());
            clothingSize.setUk(request.getClothingSizes().getUk());
            clothingSize.setUs(request.getClothingSizes().getUs());

            user.setClothingSize(clothingSize);
            userRepository.save(user);
        }

        return ResponseEntity.ok("Updated Successfully");
    }
}
