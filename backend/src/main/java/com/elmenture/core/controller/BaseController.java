package com.elmenture.core.controller;

import com.elmenture.core.engine.SizeMapper;
import com.elmenture.core.model.User;
import com.elmenture.core.payload.SizeDto;
import com.elmenture.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

import static com.elmenture.core.engine.charts.MeasurementUnit.*;

/**
 * Created by otikev on 14-Apr-2022
 */

public abstract class BaseController {

    @Autowired
    UserRepository userRepository;

    protected User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName);
        return user;
    }

    protected SizeDto getDressSizesForUser(User user){
        Map<String,String> values = SizeMapper.mappedSizesForUser(user);

        SizeDto dressDto = new SizeDto();
        if(values == null || values.size()==0){
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
}
