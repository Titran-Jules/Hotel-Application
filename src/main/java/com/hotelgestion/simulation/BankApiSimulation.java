package com.hotelgestion.simulation;

public class BankApiSimulation {
    public static boolean chargeCard(String token, double amount) {
        System.out.println("[SIMULATION]: Traitement de paiement par carte de :" + amount + " Ar.");
        return !"INVALID".equals(token);
    }

    public static boolean sendMvolaPush(String phoneNumber, double amount) {
        System.out.println("[SIMULATION MVOLA]: Envoi de push de débit de : "+amount+ " Ar au numéro "+phoneNumber+"...");
        return true;
    }
}
