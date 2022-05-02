package com.elmenture.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by otikev on 17-Mar-2022
 */

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "orders")
@NoArgsConstructor
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "state")
    private String state;

    @Column(name = "merchant_request_id")
    private String merchantRequestID;

    public Order(User user, String state, String merchantRequestID) {
        this.user = user;
        this.state = state;
        this.merchantRequestID = merchantRequestID;
    }
}