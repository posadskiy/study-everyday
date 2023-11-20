package com.posadskiy.java.release.v18;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class InetAddressResolving {

    private final static System.Logger log = System.getLogger("default");

    public static void main(String[] args) {
        final InetAddress[] allByName;
        try {
            allByName = InetAddress.getAllByName("google.com");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        Arrays.stream(allByName).forEach(value -> {
            log.log(System.Logger.Level.INFO, "Address: " + value.getHostAddress() + ", hostname: " + value.getHostName() + ", canonical hostname: " + value.getCanonicalHostName());
        });
    }
}
