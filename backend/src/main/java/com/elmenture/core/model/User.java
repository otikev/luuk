package com.elmenture.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by otikev on 06-Mar-2022
 */

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    private static final long serialVersionUID = 2396654715019746670L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "social_account_type")
    private String socialAccountType;

    @Column(name = "auth_token")
    private String authToken;

    @Column(name = "contact_phone_number")
    private String contactPhoneNumber;

    @Column(name = "mobile_money_number")
    private String mobileMoneyNumber;

    @Column(name = "physical_address")
    private String physicalAddress;

    @Column(name = "gender")
    private String gender;

    /**
     * Contains a comma separated text for the type of clothing the user
     * wants to see e.g.
     * m,f,c -> see male, female and child clothes
     */
    @Column(name = "clothing_recommendations")
    private String clothingRecommendations;

    public List<String> preferredRecommendations() {
        List<String> recommendations = new ArrayList<>();
        if (clothingRecommendations == null) {
            recommendations.add("m");
            recommendations.add("f");
            recommendations.add("c");
        } else {
            String[] exploded = clothingRecommendations.split(",");
            for (String str : exploded) {
                recommendations.add(str);
            }
        }

        return recommendations;
    }

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "body_measurements_id", referencedColumnName = "id")
    private BodyMeasurement bodyMeasurement;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clothing_size_id", referencedColumnName = "id")
    private ClothingSize clothingSize;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return authToken;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}