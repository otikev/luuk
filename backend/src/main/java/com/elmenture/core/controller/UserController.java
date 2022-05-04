package com.elmenture.core.controller;

import com.elmenture.core.model.BodyMeasurement;
import com.elmenture.core.model.ClothingSize;
import com.elmenture.core.model.User;
import com.elmenture.core.payload.SignInResponse;
import com.elmenture.core.payload.UserDetailsDto;
import com.elmenture.core.payload.UserMeasurementsDto;
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
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "measurements/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postBodyMeasurements(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserMeasurementsDto request) {
        User user = getLoggedInUser();

        if (request.getBodyMeasurements() != null) {
            BodyMeasurement measurements = user.getBodyMeasurement();
            if (measurements == null) {
                measurements = new BodyMeasurement();
            }
            measurements.setChest_cm(request.getBodyMeasurements().getChest_cm());
            measurements.setWaist_cm(request.getBodyMeasurements().getWaist_cm());
            measurements.setHips_cm(request.getBodyMeasurements().getHips_cm());
            measurements.setUser(user);

            user.setBodyMeasurement(measurements);
            userRepository.save(user);
        } else if (request.getClothingSizes() != null) {
            ClothingSize clothingSize = user.getClothingSize();
            if (clothingSize == null) {
                clothingSize = new ClothingSize();
            }
            clothingSize.setInternational(request.getClothingSizes().getInternational());
            clothingSize.setEu(request.getClothingSizes().getEu());
            clothingSize.setUk(request.getClothingSizes().getUk());
            clothingSize.setUs(request.getClothingSizes().getUs());
            clothingSize.setUser(user);

            user.setClothingSize(clothingSize);
            userRepository.save(user);
        }

        return ResponseEntity.ok("Updated Successfully");
    }

    @PostMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUserDetails(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserDetailsDto request) {
        User user = getLoggedInUser();

        if (request.getName() != null) {
            String name = request.getName().trim();
            String[] names = name.split(" ");
            if (names.length > 0) {
                user.setFirstName(names[0]);
            }
            if (names.length > 1) {
                user.setLastName(names[1]);
            }
        }
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getContactPhoneNumber() != null) user.setContactPhoneNumber(request.getContactPhoneNumber());
        if (request.getMpesaPhoneNumber() != null) user.setMobileMoneyNumber(request.getMpesaPhoneNumber());
        if (request.getTargets() != null) user.setClothingRecommendations(request.getTargets());
        if (request.getPhysicalAddress() != null) user.setPhysicalAddress(request.getPhysicalAddress());

        userRepository.save(user);

        return ResponseEntity.ok("Updated Successfully");
    }

    @GetMapping("details")
    public ResponseEntity<SignInResponse> getUserDetails() {
        User user = getLoggedInUser();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SignInResponse response = new SignInResponse();
        response = createSigninResponse(response, user);
        return ResponseEntity.ok(response);
    }
}
