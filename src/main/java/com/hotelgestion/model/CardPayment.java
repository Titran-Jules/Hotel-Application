package com.hotelgestion.model;

import com.hotelgestion.simulation.BankApiSimulation;
import lombok.*;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CardPayment implements PaymentMethode {
    private String cardToken;
    private String last4Digits;

    @Override
    public void processPayment(double amount) {
        var success = BankApiSimulation.chargeCard(this.cardToken, amount);
        if (!success) {
            throw new RuntimeException("Transaction par carte refusé.");
        }
        System.out.println("[SUCCES]: carte (finissant par : "+ last4Digits+") débitée.");
    }
}
