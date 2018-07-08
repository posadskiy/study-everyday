package dev.posadskiy.spring.balancer.storage;

import java.math.BigInteger;

public interface Storage {

    public BigInteger getAmount();
    public void addValue(BigInteger value);
    public void withdraw(BigInteger value);

}
