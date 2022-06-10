package com.elmenture.core.model;

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

    /**
     * Stores the last item id in items table that was traversed while fetching the user's queue
     */
    @Column(name = "item_queue_tracker", columnDefinition = "bigint default 0")
    private long itemQueueTracker = 0;

    /**
     * Contains a comma separated text for the type of clothing the user
     * wants to see e.g.
     * m,f,c -> see male, female and child clothes
     */
    @Column(name = "clothing_recommendations")
    private String clothingRecommendations;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private BodyMeasurement bodyMeasurement;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ClothingSize clothingSize;

    public List<String> preferredRecommendations() {
        /*
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
*/
        //TODO: only handling female clothing for now.
        List<String> targets = new ArrayList<String>();
        targets.add("f");
        return targets;
    }

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