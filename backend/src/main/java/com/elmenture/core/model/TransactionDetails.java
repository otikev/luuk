package com.elmenture.core.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Table(name = "transaction_details")
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TransactionDetails extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "merchant_request_id", nullable = false)
    private String merchantRequestId;

    @Column(name = "amount",  nullable = false)
    private double amount;

    @Column(name = "phone_number",  nullable = false)
    private String phoneNumber;

    @Column(name = "mpesa_receipt_number",  nullable = false)
    private String mpesaReceiptNumber;

    public TransactionDetails(String merchantRequestId, double amount, String phoneNumber, String mpesaReceiptNumber) {
        this.merchantRequestId = merchantRequestId;
        this.amount = amount;
        this.phoneNumber = phoneNumber;
        this.mpesaReceiptNumber = mpesaReceiptNumber;
    }
}