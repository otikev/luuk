package com.elmenture.core.controller;

import com.elmenture.core.engine.SizeMapper;
import com.elmenture.core.model.TagProperty;
import com.elmenture.core.model.User;
import com.elmenture.core.payload.*;
import com.elmenture.core.repository.TagPropertyRepository;
import com.elmenture.core.repository.UserRepository;
import com.elmenture.core.service.EmailService;
import com.elmenture.core.utils.LuukProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.elmenture.core.engine.charts.MeasurementUnit.*;

/**
 * Created by otikev on 14-Apr-2022
 */

public abstract class BaseController {

    @Autowired
    protected EmailService emailService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TagPropertyRepository tagPropertyRepository;

    protected ExecutorService executor = Executors.newFixedThreadPool(10);

    //FIXME: Hardcoding for now. This will eventually be replaced with user roles functionality
    private String[] STAFF = {
            "oti.kevin@gmail.com",
            "njihiamuchai@gmail.com",
            "aycewhispero@gmail.com",
            "kellen.kinyua@gmail.com"
    };

    protected User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName);
        return user;
    }

    protected SizeDto getDressSizesForUser(User user) {
        Map<String, String> values = SizeMapper.mappedSizesForUser(user);

        SizeDto dressDto = new SizeDto();
        if (values == null || values.size() == 0) {
            System.out.println("None of the values entered by the user can map.");
            return dressDto;
        }
        dressDto.setInternational(values.get(INT.name()));
        dressDto.setUs(Integer.parseInt(values.get(US.name())));
        dressDto.setUk(Integer.parseInt(values.get(UK.name())));
        dressDto.setEu(Integer.parseInt(values.get(EU.name())));
        dressDto.setChest_cm(values.get(BUST_CM.name()));
        dressDto.setWaist_cm(values.get(WAIST_CM.name()));
        dressDto.setHips_cm(values.get(HIPS_CM.name()));
        return dressDto;
    }

    protected SignInResponse createSigninResponse(SignInResponse response, User user) {
        UserMeasurementsDto actualMeasurements = new UserMeasurementsDto();

        BodyMeasurementsDto bodyMeasurementsDto = new BodyMeasurementsDto();
        if (user.getBodyMeasurement() != null) {
            Integer chest = user.getBodyMeasurement().getChest_cm();
            Integer waist = user.getBodyMeasurement().getWaist_cm();
            Integer hips = user.getBodyMeasurement().getHips_cm();

            if (chest != null && chest > 0) {
                bodyMeasurementsDto.setChest_cm(user.getBodyMeasurement().getChest_cm());
            }

            if (waist != null && waist > 0) {
                bodyMeasurementsDto.setWaist_cm(waist);
            }

            if (hips != null && hips > 0) {
                bodyMeasurementsDto.setHips_cm(hips);
            }
        }
        actualMeasurements.setBodyMeasurements(bodyMeasurementsDto);


        ClothingSizeDto clothingSizeDto = new ClothingSizeDto();
        if (user.getClothingSize() != null) {
            String _int = user.getClothingSize().getInternational();
            Integer us = user.getClothingSize().getUs();
            Integer uk = user.getClothingSize().getUk();
            Integer eu = user.getClothingSize().getEu();
            if (_int != null) {
                clothingSizeDto.setInternational(_int);
            }

            if (us != null) {
                clothingSizeDto.setUs(us);
            }

            if (uk != null) {
                clothingSizeDto.setUk(uk);
            }
            if (eu != null) {
                clothingSizeDto.setEu(eu);
            }
        }
        actualMeasurements.setClothingSizes(clothingSizeDto);

        SignInResponse.FemaleSize femaleSize = new SignInResponse.FemaleSize();
        femaleSize.setDress(getDressSizesForUser(user));

        String sessionKey = UUID.randomUUID().toString().replace("-", "") + ":" + user.getUsername() + ":" + user.getAuthToken();
        String sessionKeyBase64 = Base64.getEncoder().encodeToString(sessionKey.getBytes(StandardCharsets.UTF_8));
        response.setSessionKey(sessionKeyBase64);
        response.setSuccess(true);
        response.setActualMeasurements(actualMeasurements);
        response.setFemaleSize(femaleSize);
        response.setContactPhoneNumber(user.getContactPhoneNumber());
        response.setPhysicalAddress(user.getPhysicalAddress());
        response.setClothingRecommendations(user.getClothingRecommendations());
        response.setName(user.getFirstName() + " " + user.getLastName());
        response.setEmail(user.getEmail());
        if (isStaff(user)) {
            response.setStaff(true);
            response.setS3AccessKeyId(LuukProperties.amazonS3AccessKeyId);
            response.setS3SecretKeyId(LuukProperties.amazonS3SecretKeyId);
        }

        List<TagProperty> tagProperties = tagPropertyRepository.findAll();

        List<TagPropertyDto> tagPropertiesList = new ArrayList<>();
        for (TagProperty tagProperty : tagProperties) {
            TagPropertyDto dto = new TagPropertyDto();
            dto.setId(tagProperty.getId());
            dto.setValue(tagProperty.getValue());
            tagPropertiesList.add(dto);
        }
        response.setTagProperties(tagPropertiesList);
        return response;
    }


    private boolean isStaff(User user) {
        List<String> staffList = new ArrayList<>(Arrays.asList(STAFF));
        return staffList.contains(user.getEmail());
    }
}
