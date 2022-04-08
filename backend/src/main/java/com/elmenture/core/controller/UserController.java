package com.elmenture.core.controller;

import com.elmenture.core.model.BodyMeasurement;
import com.elmenture.core.model.ClothingSize;
import com.elmenture.core.model.User;
import com.elmenture.core.payload.request.UserDetails;
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
import java.util.Objects;

/**
 * Created by otikev on 06-Mar-2022
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "measurements/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postBodyMeasurements(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserMeasurementsDto request) {
        String token = MiscUtils.getUserTokenFromHeader(headers.get("luuk-x-authorization").get(0));
        User user = userRepository.findByAuthToken(token);
        if (user == null) {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }
        if (request.getBodyMeasurement() != null) {
            BodyMeasurement measurements = user.getBodyMeasurement();
            if (measurements == null) {
                measurements = new BodyMeasurement();
            }
            measurements.setChest_cm(request.getBodyMeasurement().getChest_cm());
            measurements.setWaist_cm(request.getBodyMeasurement().getWaist_cm());
            measurements.setHips_cm(request.getBodyMeasurement().getHips_cm());

            user.setBodyMeasurement(measurements);
            userRepository.save(user);
        } else if (request.getClothingSize() != null) {
            ClothingSize clothingSize = user.getClothingSize();
            if (clothingSize == null) {
                clothingSize = new ClothingSize();
            }
            clothingSize.setInternational(request.getClothingSize().getInternational());
            clothingSize.setEu(request.getClothingSize().getEu());
            clothingSize.setUk(request.getClothingSize().getUk());
            clothingSize.setUs(request.getClothingSize().getUs());

            user.setClothingSize(clothingSize);
            userRepository.save(user);
        }

        return ResponseEntity.ok("Updated Successfully");
    }

    @PostMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUserDetails(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserDetails request) {
        String token = MiscUtils.getUserTokenFromHeader(headers.get("luuk-x-authorization").get(0));
        User user = userRepository.findByAuthToken(token);
        if (user == null || request == null) {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }

        if (request.getName() != null) user.setFirstName(request.getName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getContactPhoneNumber() != null)user.setContactPhoneNumber(request.getContactPhoneNumber());
        if (request.getMpesaPhoneNumber() != null) user.setMobileMoneyNumber(request.getMpesaPhoneNumber());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getPhysicalAddress() != null) user.setPhysicalAddress(request.getPhysicalAddress());

        userRepository.save(user);

        return ResponseEntity.ok("Updated Successfully");
    }
}
