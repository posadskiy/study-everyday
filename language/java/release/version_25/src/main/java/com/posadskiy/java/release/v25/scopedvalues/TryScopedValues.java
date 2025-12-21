package com.posadskiy.java.release.v25.scopedvalues;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class TryScopedValues {
    private static final ScopedValue<Long> USERS_ID = ScopedValue.newInstance();
    private static final ScopedValue<Double> AMOUNT = ScopedValue.newInstance();
    
    public void createTransaction(Long customerId, Double amount) {
        ScopedValue.where(USERS_ID, customerId).run(() ->
            ScopedValue.where(AMOUNT, amount).run(TryScopedValues::transactionCreation)
        );
    }
    
    public static void transactionCreation() {
        // loading customer profile
        
        // some validations
        
        log.info("Customer ID is {}, amount is {}", USERS_ID.get(), AMOUNT.get());
    }
    
    public static void main(String[] args) {
        var tryScopedValues = new TryScopedValues();
        tryScopedValues.createTransaction(123L, 1000.0);
        tryScopedValues.createTransaction(312L, 2000.0);
    }
}
