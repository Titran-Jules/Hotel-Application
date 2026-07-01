package com.hotelgestion.model;

import com.hotelgestion.simulation.BankApiSimulation;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MvolaPayment implements PaymentMethode {
    private String phoneNumber;
    private String transactionId;

    @Override
    public void processPayment(double amount) {
        boolean pushSent = BankApiSimulation.chargeCard(this.phoneNumber, amount);

        if (!pushSent) {
            throw new RuntimeException("Impossible de joindre le serveur Mvola");
        }

        this.transactionId = "TXN-"+ System.currentTimeMillis();
        System.out.println("[SUCCES] Mvola payé ! Ref transaction : "+ this.transactionId);
    }
}
